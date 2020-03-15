package base.chapter4.dot_4

/**
 * Created by dumingwei on 2018/1/18 0018.
 * 单例模式
 */
//这种声明方式为 饿汉模式
object Singleton {

    fun test() {
        println("hello singleton")
    }
}

//这种声明方式为 懒汉模式
/*
class SingletonLazy private constructor() {

    companion object {
        val INSTANCE: SingletonLazy by lazy { SingletonLazy() }
    }

    fun check() {
        println("hello SingletonLazy")
    }
}
*/

fun main(args: Array<String>) {
    Singleton.test()
    //SingletonLazy.INSTANCE.check()
}
