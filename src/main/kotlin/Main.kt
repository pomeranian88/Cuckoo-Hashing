/*
 * Author: Zev Thompson
 * 
 * Collab Statement: Wrote the code by myself but talked through the logic of the get and set functions with Ian.
 * 
 * Reflection:  Was not so hard, mainly just in understanding how arrays work, and drawing out how hash tables work here.
 *              But because no collision detection was needed it was fairly simple. Took around 1 hour.
 */

fun main() {
    val map = CuckooHashMap<Int, String>(20)
    map.set(1, "hello")
    println(map.get(1))
    map.display()
}
