package leetcode

/**
 * Crete by dumingwei on 2020-02-12
 * Desc: 回文数
 */
object PalindromeNumberTest {

    @JvmStatic
    fun main(args: Array<String>) {

        println(isPalindrome(1))
        println(isPalindrome(12))
        println(isPalindrome(11))
        println(isPalindrome(12321))
        println(isPalindrome(123321))
        println(isPalindrome(123421))
    }

    /**
     * 一个回文数，把数字转化成字符串的话，从中间分开，左边从现向后遍历，
     * 右边从后向前遍历？
     *
     *
     *
     *
     * 12321  2 12
     * 123321 3 123 543
     *
     * @param number
     * @return
     */
    fun isPalindrome(number: Int): Boolean {
        val numberStr = number.toString()
        val length = numberStr.length
        if (length == 1) {
            return true
        }
        var result = true

        val middle = length / 2

        for (i in 0 until middle) {
            val left = numberStr[i]
            val right = numberStr[length - 1 - i]
            if (left != right) {
                result = false
                break
            }
        }
        return result
    }

}
