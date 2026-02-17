
fun main() {
    val map = CuckooHashMap<Int, String>(20)
    map.set(1, "hello")
    println(map.get(1))
    map.display()
}
