package base.chapter8.dot_2

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by dumingwei on 2018/1/7 0007.
 */

/**
 * 定义一个线程安全的方法
 */
inline fun <T> threadSafeMethod(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

fun main() {


    arrayListOf<String>("hello", "world")
        .map {
            it + "haha"
        }
    foo(ReentrantLock())

    /*LockOwner(ReentrantLock()).runUnderLock {
        println("LockOwner")
    }*/

    //fooTwo(ReentrantLock()) { println("foo Two action") }
}

fun foo(l: Lock) {
    threadSafeMethod(l) {
        println("Action one")
    }

    threadSafeMethod(l) {
        println("Action two")
    }

}

fun fooTwo(l: Lock, body: () -> Unit) {
    println("Before sync")
    threadSafeMethod(l, body)
    println("After sync")
}

class LockOwner(val lock: Lock) {

    fun runUnderLock(body: () -> Unit) {
        threadSafeMethod(lock, body)
    }
}
