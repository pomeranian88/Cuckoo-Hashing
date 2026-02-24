import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import kotlin.test.fail
import kotlin.random.Random

class HashTest {
    @Test
    fun testGeneralFunctionality() {
        val map = CuckooHashMap<Int, String>(20)
        val max = 20
        for (key in 0..max step 2) {
            map.set(key, "k" + key)
        }
        for (key in 0..max step 2) {
            assertEquals("k$key", map.get(key))
        }

        for (key in 1..max step 2) {
            assertNull(map.get(key))
        }
    }


    val seededRandom = Random(12345)

    fun randomString(length: Int): String {
        val validChars: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return CharArray(length) { validChars.random(seededRandom) }
            .concatToString()
    }


    // Checks that you do a lookup in no more than two checks
    fun insertAndLookupCheck(tableSize: Int, numTestEntries: Int) {
        val map = CuckooHashMap<Int, String>(tableSize)
        // Verify all entries are null
        for (tableNum in 0..1) {
            for (entry in map.tables[tableNum]) {
                assertNull(entry)
            }
        }

        // Use a built-in map to store values to check they are right
        val testEntries = mutableMapOf<Int, String>()

        // Insert a large number of objects. Check that they all make it in, and
        // that they can all be found within two lookups.
        for (i in 0..<numTestEntries) {
            val key = seededRandom.nextInt()
            val value = randomString(10)
            map.set(key, value)
            testEntries.set(key, value)
            // println("Inserting $key $value")
        }

        // Verify that each of the two tables is still the right size,
        // i.e., it hasn't been extended in appropriately
        for (tableNum in 0..1) {
            assertEquals(tableSize, map.tables[tableNum].count())
        }

        // Verify can find each key and value
        keyloop@
        for ((testKey, testValue) in testEntries) {
            for (table in 0..1) {
                val hashCode = map.cuckooHashCode(table, testKey)
                val index = hashCode % tableSize
                val entry = map.tables[table][index]
                if (entry != null && entry.key == testKey) {
                    assertEquals(testValue, entry.value)
                    // println("Found it $key ${entry.value}} $table")
                    continue@keyloop
                }
            }
            // did not find it
            map.display()
            fail("Did not find key $testKey with $testValue")
        }
    }

    @Test
    fun oneTableNoRehashingNeeded() {
        for (i in 0..<1000) {
            insertAndLookupCheck(2, 1)
            insertAndLookupCheck(10, 1)
            insertAndLookupCheck(20, 1)
        }
    }

    @Test
    fun noRehashingNeeded() {
        for (i in 0..<1000) {
            insertAndLookupCheck(2, 2)
            insertAndLookupCheck(10, 2)
            insertAndLookupCheck(20, 2)
        }
    }

    @Test
    fun rehashingLikely() {
        // Cuckoo hashing has high probability of collisions when load factor
        // is over 1/2 (Pugh paper says this without justification), so
        // keep it low
        for (i in 0..<1000) {
            insertAndLookupCheck(10, 3)
            insertAndLookupCheck(20, 7)
            insertAndLookupCheck(50, 20)
        }
    }
}
