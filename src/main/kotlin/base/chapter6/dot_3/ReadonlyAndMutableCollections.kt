package base.chapter6.dot_3

/**
 * Created by dumingwei on 2018/1/2 0002.
 */
fun <T> copyElements(source: Collection<T>, target: MutableCollection<T>) {

    for (item in source) {
        target.add(item)
    }
}

fun main(args: Array<String>) {
    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    println(target)

    val list = arrayListOf<Int>(1, 2)
    list += 3
    val newList = list + listOf<Int>(4, 5)
    println(newList)


}

fun test() {
    //不可变集合，只能获取，不能添加
    val list: List<String> = listOf("a", "b", "c")
    val map: Map<String, String> = mapOf("key" to "value", "key1" to "value1")

    val mutableList: MutableList<String> = arrayListOf("a", "b", "c")
    mutableList.add("d")
    mutableList.add("e")
    mutableList.add("f")

    val hashMap: HashMap<String, String> = hashMapOf()
    hashMap["key"] = "value"

}