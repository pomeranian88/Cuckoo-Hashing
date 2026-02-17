import kotlin.math.abs
import kotlin.random.Random

class CuckooHashMap<K, V>(tableSize: Int) : CuckooSetup<K, V>(tableSize) {

    // Look up a key, and get the corresponding value. Try both tables as
    // needed. Returns null if not found.
    fun get(key: K): V? {
        var code: Int = cuckooHashCode(0, key) % tableSize // get the code of the key for table 0
        if(tables[0][code]==null){                         // if theres nothing there...
             code = cuckooHashCode(1, key) % tableSize     //   get the code of the key for table 1
             return tables[1][code]?.value                 //   and return its value at table 1 even if its null
        }
        return tables[0][code]?.value                      // otherwise return the value at table 0, and we know its not null
    }  

    // Adds a key and a value to one of the hash tables.
    fun set(key: K, value: V) { // for now, only inserts to table 0
        val code: Int = cuckooHashCode(0, key) % tableSize  // get the corresponding index for table 0
        tables[0][code] = Entry(key, value)                 // and plug the key in at that index
    }


}
