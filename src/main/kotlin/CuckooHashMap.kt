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
        
        // if there are two collisions, we now have attempt = the abandoned one, and rehash the whole table :(
        if(attempt!=null){
            rehashUntilSuccess(attempt!!.key, attempt!!.value)
        }
    }

    // Does the work of inserting into the tables.
    fun insert(key: K, value: V): Entry<K,V>?{
        var temp: Entry<K, V>? = null
        var tempAbandoned: Entry<K, V>? = null // only used when there are 2+ collisions
        if(key==299063613) println("HIHIHIHIHIHIHIHI")

        var code1: Int = cuckooHashCode(tableNum=0, key) % tableSize
        if(key==299063613) println(code1)
        if(key==299063613) nownownow=true

        if(tables[0][code1]==null){
            tables[0][code1] = Entry(key, value)
            if(nownownow) display()
            return null
        }

        // attempts to insert at first table: simply insert at 0
        if(tables[0][code1]?.key == key){
            tables[0][code1] = Entry(key, value)
            if(nownownow) display()
            return null
        }
        else{
            val code2: Int = cuckooHashCode(tableNum=1, tables[0][code1]!!.key) % tableSize

            // 1 is full, attempts to insert at second: kick value from 0 to 1, add value to 0
            if(tables[1][code2]==null){
                temp = tables[0][code1]
                tables[0][code1] = Entry(key, value)        // table 0 & index corresponding to the new one = new one
                tables[1][code2] = temp                     // table 1 & index corresponding to kicked baby = kicked baby

                if(nownownow) display()
                return null
            }

            // 0 & 1 are full, kick and kick and save final value: then rehash and put into 0
            else{
                temp = tables[0][code1]
                tempAbandoned = tables[1][code2]
                tables[0][code1] = Entry(key, value)
                tables[1][code2] = temp

                if(nownownow) display()
                return tempAbandoned
            }
        }

        // if succeed
        return null
    }

    // rehash once
    fun rehash(key: K, value: V): Entry<K,V>?{
        seed++

        val current = Entry(key, value)

        var everythingEver = mutableListOf<Entry<K,V>>()
        everythingEver.add(current)
        for(table in 0 .. 1){
            for(entry in tables[table]){
                if(entry!=null){
                    everythingEver.add(entry)
                }
            }
        }

        for(table in 0..1){
            for(i in tables[table].indices){
                tables[table][i] = null
            }
        }
        
        for(entry in everythingEver){
            val kicked = insert(entry.key, entry.value)
            if(kicked!=null){
                return kicked
            }
        }
        return null
    }

    fun rehashUntilSuccess(key: K, value: V){
        var attempts: Int = 0
        var current: Entry<K, V>? = Entry(key, value)

        while(current!=null && attempts<MAX_LOOP){
            current = rehash(current.key, current.value)
            attempts++
        }
    }
}
