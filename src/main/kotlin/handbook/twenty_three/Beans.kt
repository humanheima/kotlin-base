package handbook.twenty_three

/**
 *
 * Created by dumingwei on 2019/1/9
 *
 * Desc:
 */


class UserWrapper {

    private val address = Address()

    var name: String? = null
    var password: String? = null

    fun address(init: Address.() -> Unit): Address { // 类似于 apply 函数，返回 address 对象本身

        address.init()
        return address
    }

    internal fun getAddress() = address
}

class User {

    var name: String? = null
    var password: String? = null
    var addresses: Address? = null

    override fun toString() = "name=$name,password=$password"
}


class Address {

    var province: String? = null
    var city: String? = null
    var street: String? = null

    override fun toString() = "province=$province,city=$city,street=$street"
}