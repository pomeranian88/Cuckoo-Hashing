/*
 * Author: Zev Thompson
 * 
 * Collab Statement: Wrote the code by myself but talked through the logic of the get and set functions with Ian.
 * 
 * Reflection:  Was not so hard, mainly just in understanding how arrays work, and drawing out how hash tables work here.
 *              But because no collision detection was needed it was fairly simple. Took around 1 hour.
 */

fun main() {
    // val map = CuckooHashMap<Int, String>(20)
    // map.set(1, "hello")
    // println(map.get(1))
    // map.display()
    // map.set(1, "bang")
    // println(map.get(1))
    // map.display()
    // map.set(1, "skeedat")
    // println(map.get(1))
    // map.display()
    // map.set(1, "slammer")
    // println(map.get(1))
    // map.display()

    val map = CuckooHashMap<Int, String>(20)
    val max = 20
    for (key in 0..max step 2) {
        map.set(key, "k" + key)
        // map.display()
    }
    map.display()

    for (key in 0..max step 2) {
        
        println("k$key: table 0-${map.cuckooHashCode(0, key)%map.tableSize} , table1-${map.cuckooHashCode(1, key)%map.tableSize} , -${map.get(key)}")
    }

    for (key in 1..max step 2) {
        println("${map.get(key)}")
    }
}
