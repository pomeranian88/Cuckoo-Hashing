/*
 * Author: Zev Thompson
 * 
 * DISCLAIMER -- I realize that this doesn't work, and I only realized late into my debugging that I was thinking about it wrong. I'd
 *               been working with the three cases (open spot in 0, open spot in 1, or double kick), and not considering two things:
 *               1. that I need to reshash the ENTIRE table if I ever run into a cycle, and 2. that with this knowledge, there is the
 *               possibility of something getting kicked around and around 2+ times, without having to rehash the entire table at the
 *               moment that you're at 2+ collisions. 
 * 
 *               Because I was instead working with cases, at the case where I was getting to the double kick, I would rehash the
 *               abandoned piece into the first table with a different seed than everything else, which would in turn lose the whole table.
 * 
 *               To fix this I'll have to fully rewrite the insert() function to be a while loop. :(
 * 
 * 
 * Collab Statement for pt 2: Wrote the code for get() & insert() by myself, wrote the code for rehash() & rehashUntilSuccess() with
 *                            help from Ian. Collaborated with Ben throughout the whole process but it kiiiind of led nowhere -- we were
 *                            both working with the wrong starting logic. And went to office hours, where I collaborated briefly with
 *                            Seraphina & talked through re-seeding with Jean.
 * 
 * Reflection for pt 2: I ran into a lot of challenges with this project, and some of them were definitely with starting late into
 *                      the process, and having to rush to understand the pre-written code, which ended up causing my initial failure
 *                      in logic that messed up the rest of the project. I'm going to fix a lot of these mistakes for the re-do.
 *                      I worked on this for like 12 hours.
 */

fun main() {
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
