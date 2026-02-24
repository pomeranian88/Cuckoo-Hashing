import kotlin.math.abs
import kotlin.random.Random

class CuckooHashMap<K, V>(tableSize: Int) : CuckooSetup<K, V>(tableSize) {
    var nownownow: Boolean = false

    // Look up a key, and get the corresponding value. Try both tables as
    // needed. Returns null if not found.
    fun get(key: K): V? {
        var code: Int = cuckooHashCode(0, key) % tableSize                                           // get the code of the key for table 0
        if(tables[0][code]!=null && tables[0][code]?.key == key){return tables[0][code]?.value}      // return (only the value!) it if it matches the key

        code = cuckooHashCode(1, key) % tableSize                                                    // get the code of the key for table 1
        if(tables[1][code]!=null && tables[1][code]?.key == key){return tables[1][code]?.value}      // return (only the value!) it if it matches the key

        return null     // else it's not there
    }  

    // Adds a key and a value to one of the hash tables.
    fun set(key: K, value: V) {
        var attempt: Entry<K,V>? = insert(key, value)

        // if you are able to successfully insert (if attempt==null), do nothing
        
        // if there are two collisions, we now have attempt = the abandoned one, and rehash the whole table! (--this is one of the parts where I went wrong--)
        if(attempt!=null){
            rehashUntilSuccess(attempt!!.key, attempt!!.value)
        }
    }

    // Does the work of inserting into the tables.
    fun insert(key: K, value: V): Entry<K,V>?{
        var temp: Entry<K, V>? = null
        var tempAbandoned: Entry<K, V>? = null // only used when there are 2+ collisions

        var code1: Int = cuckooHashCode(tableNum=0, key) % tableSize

        var counter: Int = 0
        var current = Entry(key, value)
        var table: Int = 0 // only ever 0 or 1

        // specific case in case you run into something where multiple keys are the same
        if(tables[0][code1]?.key == key){
            tables[0][code1] = Entry(key, value)

            return null
        }
        // CASE ONE: attempt to insert at first table: simply insert at 0
        if(tables[0][code1]==null){
            tables[0][code1] = Entry(key, value)

            return null
        }
        // CASES TWO & THREE
        else{
            val code2: Int = cuckooHashCode(tableNum=1, tables[0][code1]!!.key) % tableSize

            // CASE TWO: 1 is full, attempts to insert at second: kick value from 0 to 1, add value to 0
            if(tables[1][code2]==null){
                temp = tables[0][code1]
                tables[0][code1] = Entry(key, value)        // table 0 & index corresponding to the new one = new one
                tables[1][code2] = temp                     // table 1 & index corresponding to kicked baby = kicked baby

                return null
            }
            // CASE THREE: 0 & 1 are full, kick and kick and save final value: then will end up rehashing and put into 0
            else{
                temp = tables[0][code1]
                tempAbandoned = tables[1][code2]
                tables[0][code1] = Entry(key, value)
                tables[1][code2] = temp

                return tempAbandoned
            }
        }
    }

    // Rehash the entire table in one go
    fun rehash(key: K, value: V): Entry<K,V>?{
        val tempo = Entry(key, value)   // take the abandoned value and give it an Entry
        seed++                          // the key bit -- change the seed

        var allItems = mutableListOf<Entry<K,V>>()  // make a mutable list of entries...
        allItems.add(tempo)                      
        for(i in 0 .. 1){                       // and add every item in the hashtable to it,
            for(item in tables[i]){
                if(item!=null){
                    allItems.add(item)
                }
            }
        }

        for(table in 0..1){                     // then in similar fashion completely reset the hashtable,
            for(i in tables[table].indices){
                tables[table][i] = null
            }
        }
        
        for(item in allItems){                  // now go back through all the saved items, and reinsert them all.
            val leftoverCurrent = insert(item.key, item.value)
            if(leftoverCurrent!=null){
                return leftoverCurrent          // this means insert failed: return a non null, then rehashUntilSuccess will continue
            }
        }
        return null                             // this means success -- everything was rehashed successfully!
    }

    fun rehashUntilSuccess(key: K, value: V){
        var counter: Int = 0
        var tempo: Entry<K, V>? = Entry(key, value)

        while(tempo!=null && counter<MAX_LOOP){     // while the rehash function hasn't returned an abandoned double kicked piece, &
            tempo = rehash(tempo.key, tempo.value)  // you haven't looped too many times, keep looping until you receieve a null
            counter++                               // which means everything was rehashed successfully
        }
    }
}