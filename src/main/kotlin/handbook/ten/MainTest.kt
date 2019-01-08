package handbook.ten

import kotlin.reflect.KProperty

/**
 * Created by dmw on 2019/1/7.
 * Desc:委托、运算符重载以及中缀表达式（一）
 */
fun main(args: Array<String>) {

    val base = BaseImpl(10)
    Derived(base).print()

    val student1 = Student(1, StdMarks(), StdTotals()) // StdMarks、StdTotals 为 Student 被委托的类
    student1.printMarks()
    student1.printTotals()

    println("---------------------------")

    val student2 = Student(2, ExcelMarks(), ExcelTotals()) // ExcelMarks、ExcelTotals 为 Student 被委托的类
    student2.printMarks()
    student2.printTotals()
}



interface Marks {

    fun printMarks()
}

class StdMarks : Marks {

    override fun printMarks() = println("printed marks")
}

class ExcelMarks : Marks {

    override fun printMarks() = println("printed marks and export to excel")
}

interface Totals {

    fun printTotals()
}

class StdTotals : Totals {

    override fun printTotals() = println("calculated and printed totals")
}

class ExcelTotals : Totals {

    override fun printTotals() = println("calculated and printed totals and export to excel")
}

class Student(studentId: Int, marks: Marks, totals: Totals) : Marks by marks, Totals by totals


// 创建接口
interface Base {
    fun print()
}

// 实现此接口的被委托的类
class BaseImpl(val x: Int) : Base {
    override fun print() {
        print(x)
    }
}

/**
 * 通过关键字 by 完成委托，Derived 相当于代理类
 */
class Derived(b: Base) : Base by b

