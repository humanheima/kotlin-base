package base.chapter3.dot_1

/**
 * Created by dumingwei on 2017/12/22 0022.
 */

val hashSet = hashSetOf(1, 7, 53)
val list = listOf(1, 7, 53)
val arrayList = arrayListOf(1, 7, 53)
val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
fun main(args: Array<String>) {
//    println(hashSet.javaClass)
//    println(list.javaClass)
//    println(arrayList.javaClass)
//    println(map.javaClass)
//    println(hashSet.max())
//    println(list.last())

    //testFold()
    //testFoldIndexed()
    //testFoldRight()
    testFoldRightIndexed()

}

fun testFold() {
    val list = listOf(1, 2, 3, 4)
    val result = list.fold(2) { acc: Int, i: Int ->
        val nextAcc = acc + i
        println("nextAcc = $nextAcc")
        return@fold nextAcc
    }
    println("result = $result")
}

fun testFoldIndexed() {
    val list = listOf(1, 2, 3, 4)
    val result = list.foldIndexed(2) { index: Int, acc: Int, i: Int ->
        val nextAcc = acc + i
        println("index = $index ，nextAcc = $nextAcc")
        return@foldIndexed nextAcc
    }
    println("result = $result")
}

fun testFoldRight() {
    val list = listOf(1, 2, 3, 4)
    val result = list.foldRight(2) { acc: Int, i: Int ->
        val nextAcc = acc + i
        println("nextAcc = $nextAcc")
        return@foldRight nextAcc
    }
    println("result = $result")
}

fun testFoldRightIndexed() {
    val list = listOf(1, 2, 3, 4)
    val result = list.foldRightIndexed(2) { index: Int, acc: Int, i: Int ->
        val nextAcc = acc + i
        println("index = $index ，nextAcc = $nextAcc")
        return@foldRightIndexed nextAcc
    }
    println("result = $result")
}
