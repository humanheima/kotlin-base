package base.chapter4.dot_3

/**
 * Created by dumingwei on 2017/12/23 0023.
 */
class CountingSet<T>(
    val innerSet: MutableCollection<T> = HashSet()
) : MutableCollection<T> by innerSet {

    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        println("invoke add")
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}

fun main(args: Array<String>) {
    val cset = CountingSet<Int>()
    cset.add(4)
    cset.addAll(listOf(1, 1, 2))
    println("${cset.objectsAdded} objects were added,${cset.size} remain")
}