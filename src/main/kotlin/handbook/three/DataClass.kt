package handbook.three

/**
 * Created by dmw on 2019/1/3.
 * Desc:
 */

class Student {

    companion object {

        private var username = "Tony"
        private var marks = "A"

        @JvmStatic
        fun printMarks() = "The ${this.username}'s mark is ${this.marks}"

        @JvmStatic
        fun changeMarks(marks: String) {
            this.marks = marks
        }

    }
}

data class User(var name:String,var password:String)