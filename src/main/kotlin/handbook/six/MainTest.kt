package handbook.six

import java.util.*


/**
 *
 * Created by dumingwei on 2019/1/3
 *
 * Desc:
 */

fun main(args: Array<String>) {

    val sum = {
            x: Int, y: Int -> x + y
    }

    println(sum(3, 5)) // 8
    println(sum(4, 6)) // 10
    val u1 = User("tony")
    val u2 = User("cafei")
    val u3 = User("aaron")

    val users = Arrays.asList(u1, u2, u3)

    /**
     * Java写法
     */
    Collections.sort(users, object : Comparator<User> {
        override fun compare(o1: User, o2: User): Int {
            return o1.name.compareTo(o2.name)
        }

    })


    /**
     * 使用Lambda表达式
     */
    //Collections.sort(users) { o1, o2 -> o1.name.compareTo(o2.name) }

    /**
     * 使用方法引用
     * 下面的代码还用到了 Java 8 新增的 Comparator.comparing()方法
     */
    Collections.sort(users, Comparator.comparing(User::name))
}

data class User(var name: String)