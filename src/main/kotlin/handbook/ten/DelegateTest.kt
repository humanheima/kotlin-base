package handbook.ten

import kotlin.reflect.KProperty

/**
 * Created by dmw on 2019/1/7.
 * Desc:
 */
fun main(args: Array<String>) {
    val u = User()

    println(u.name)
    u.name = "Tony"

    println(u.password)
    u.password = "123456"
}

class Delegate {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "${property.name}: $thisRef"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("value=$value")
    }

}

class User {
    var name: String by Delegate()
    var password: String by Delegate()
}
