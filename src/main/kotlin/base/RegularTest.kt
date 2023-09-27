package base

/**
 * 测试正则表达式的一些使用
 */
class RegularTest {



}

fun main(){



}

/**
 * 使用正则表达式来分割字符串
 */
private fun parsePathRegular(path: String) {

    val string = "\uD83D\uDE0A《xxx书名》\uD83D\uDE0A，XXXXX#xx001#"

    val firstPattenString = "\uD83D\uDE0A《"
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, fileName, extension) = matchResult.destructured
        println("Dir:$directory")
        println("name:$fileName")
        println("ext:$extension")
    }

}