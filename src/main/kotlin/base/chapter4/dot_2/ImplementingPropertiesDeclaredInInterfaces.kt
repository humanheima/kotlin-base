package base.chapter4.dot_2

/**
 * Created by dumingwei on 2017/12/31 0031.
 * 实现在接口中声明的属性
 */
interface UserInter {
    //定义抽象属性
    val nickname: String
}

class PrivateUser(override val nickname: String) : UserInter

class SubscribingUser(val email: String) : UserInter {
    override val nickname: String
        get() = email.substringBefore('@')
}

class FacebookUser(val accountId: Int) : UserInter {
    override val nickname = getFacebookName(accountId)
}

fun getFacebookName(accountId: Int): String {
    return "fb:$accountId"
}

fun main(args: Array<String>) {
    println(PrivateUser("check@kotlinlang.org").nickname)
    println(SubscribingUser("check@kotlinlang.org").nickname)
}