package handbook.three

/**
 * Created by dmw on 2019/1/3.
 * Desc:
 */
fun main(args: Array<String>) {
    Student.changeMarks("B")
    println(Student.printMarks())

    Student.changeMarks("C")
    println(Student.printMarks())

    val user1 = User("tony", "123456")
    val user3 = user1.copy("monica")
    println(user3)

}

