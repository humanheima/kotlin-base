package base.chapter9.dot_3

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass


interface FieldValidator<in T> {

    fun validate(input: T): Boolean
}

object DefaultStringValidator : FieldValidator<String> {
    override fun validate(input: String): Boolean {
        return input.isNotEmpty()
    }
}

object DefaultIntValidator : FieldValidator<Int> {
    override fun validate(input: Int): Boolean {
        return input >= 0
    }
}

object Validators {

    private val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()

    fun <T : Any> registerValidator(kClass: KClass<T>, fieldValidator: FieldValidator<T>) {

        validators[kClass] = fieldValidator
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(kClass: KClass<T>): FieldValidator<T> = validators[kClass] as? FieldValidator<T>
        ?: throw IllegalArgumentException("No validator for ${kClass.simpleName}")
}

fun main(args: Array<String>) {

    val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()
    validators[String::class] = DefaultStringValidator
    validators[Int::class] = DefaultIntValidator

    val validator: FieldValidator<*>? = validators[String::class]

    (validator as? FieldValidator<String>)?.validate("")

    //也可以编译，运行时类型参数都被擦除了，转换可以成功，但是验证的时候会失败。java.lang.Integer cannot be cast to java.lang.String
    val value = (validator as? FieldValidator<Int>)
    value?.validate(3)


    Validators.registerValidator(String::class, DefaultStringValidator)
    Validators.registerValidator(Int::class, DefaultIntValidator)

    println(Validators[Int::class].validate(42))


}

