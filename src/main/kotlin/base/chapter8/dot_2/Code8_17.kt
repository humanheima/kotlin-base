package base.chapter8.dot_2

import java.io.BufferedReader
import java.io.FileReader
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Created by dumingwei on 2018/1/7 0007.
 * 使用内联lambda管理资源
 */
val l: Lock = ReentrantLock()

fun readFirstLineFromFile(path: String): String {
    //use内部自动处理了try/catch/finally
    BufferedReader(FileReader(path)).use { br: BufferedReader -> return br.readLine() }
}

fun main(args: Array<String>) {
    l.withLock {

    }
}