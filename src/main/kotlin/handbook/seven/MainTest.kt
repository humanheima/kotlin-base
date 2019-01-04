package handbook.seven

import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


/**
 *
 * Created by dumingwei on 2019/1/3
 *
 * Desc: 内联函数
 * 内联函数的特性
 * 1 内联函数中有函数类型的参数，那么该函数类型的参数默认也是内联的。除非，显示使用noinline进行修饰，那样该函数类型就不再是内联的。
 * 2 内联函数的优点是效率高、运行速度快。
 * 3 内联函数的缺点是编译器会生成比较多的代码，所以内联函数不要乱用。
 *
 * 非局部返回
 *
 * crossinline的使用
 */

fun main(args: Array<String>) {

    val sourceString = "write something to test.txt"
    val sourceByte = sourceString.toByteArray()
    val file = File("test.txt")
    if (!file.exists()) {
        file.createNewFile()
    }
    FileOutputStream(file).use {
        // 使用了扩展函数 use 之后，就无需再主动关闭FileOutputStream
        it.write(sourceByte)
    }

    noInlined {
        println("do something with nonInlined")
    }

    inlined {
        println("do something with inlined")
    }

}

/**
 * 在 Lambda 表达式内部不能让外部函数返回，所以在 Lambda 表达式中使用 return 是被禁止的。
 */
fun foo() {

    normalFunction {
        //return // ERROR
    }
}

fun normalFunction(function: () -> Unit) {

}

/**
 * 但是，由于内联函数的特性，可以在 Lambda 表达式中使用 return 返回外部函数。这种返回方式被称作非局部返回(non-local returns)。
 */
fun foo1() {
    inlineFunction {
        return //  OK
    }
}

inline fun inlineFunction(function: () -> Unit) {

}


inline fun <T : Closeable?, R> T.use(block: (T) -> R): R {

    var closed = false

    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            this?.close()
        } catch (closeException: Exception) {

        }
        throw e

    } finally {
        if (!closed) {
            this?.close()
        }
    }
}

/**
 * 不使用内联函数
 */
fun noInlined(block: () -> Unit) {
    block()
}

/**
 * 使用内联函数
 */
inline fun inlined(block: () -> Unit) {
    block()
}




