package base.chapter5.dot_4

/**
 * Created by dumingwei on 2017/12/26 0026.
 * 使用SMA构造方法来返回值
 */

fun createAllDoneRunnable(): Runnable {
    return Runnable { println("All done!") }
}

fun postponeComputation(id: Int, runnable: Runnable) {


}

val runnable = Runnable { println(42) }
fun handleComputation() {

    postponeComputation(1000, runnable)

}

fun main(args: Array<String>) {
    //createAllDoneRunnable().run()
    handleComputation()
}