import kotlin.math.abs
import kotlin.random.Random

class CuckooHashMap<K, V>(tableSize: Int) : CuckooSetup<K, V>(tableSize) {

    // Look up a key, and get the corresponding value. Try both tables as
    // needed. Returns null if not found.
    fun get(key: K): V? {
        var code: Int = cuckooHashCode(0, key) % tableSize
        if(tables[0][code]==null){
             code = cuckooHashCode(1, key) % tableSize
             return tables[1][code]?.value
        }
        return tables[0][code]?.value
    }

    // Adds a key and a value to one of the hash tables.
    fun set(key: K, value: V) {
        val code: Int = cuckooHashCode(0, key) % tableSize
        tables[0][code] = Entry(key, value)
    }


}
