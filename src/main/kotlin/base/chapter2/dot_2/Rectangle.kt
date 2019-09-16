package base.chapter2.dot_2

import java.util.*

/**
 * Created by dumingwei on 2017/12/19 0019.
 * 自定义访问器
 */
class Rectangle(val height: Int, val width: Int) {

    /**
     * isSquare是实时计算出来的
     */
    val isSquare: Boolean
        get() {
            return height == width
        }
}

fun createRandomRectangle(): Rectangle {
    val random = Random()
    return Rectangle(random.nextInt(), random.nextInt())
}