package base

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by dumingwei on 2022/4/15.
 *
 * Desc:
 */

@ExperimentalStdlibApi
fun main() {
    val person = Person2()
    person.name = "peter"
    person.lastname = "wang"
    println("name=${person.name}")
    println("lastname=${person.lastname}")
    println("updateCount=${person.updateCount}")
}

/**
 *  类委托
 */
class CountingSet3<T>(
    val innerSet: MutableSet<T> = HashSet<T>()

) : MutableSet<T> by innerSet {

    var objectAdded = 0

    override fun add(element: T): Boolean {
        objectAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectAdded += elements.size
        return innerSet.addAll(elements)
    }

}

/**
 * 委托属性
 */

class Person2 {
    @ExperimentalStdlibApi
    var name: String by Delegate3()

    @ExperimentalStdlibApi
    var lastname: String by Delegate3()
    var updateCount: Int = 0
}

class Student {
    var name: String = ""

    var address: String = ""
}

class Delegate3 : ReadWriteProperty<Any, String> {
    var formattedString = ""
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return formattedString + "-" + formattedString.length
    }

    //@ExperimentalStdlibApi
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        if (thisRef is Person2) {
            thisRef.updateCount++
        }
        formattedString = value.lowercase().replaceFirstChar { it.uppercase() }
    }

}





