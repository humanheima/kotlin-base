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
        ?: throw IllegalArgumentException("No validator for ${kClass.sealedSubclasses}")
}

fun main(args: Array<String>) {

    Validators.registerValidator(String::class, DefaultStringValidator)
    Validators.registerValidator(Int::class, DefaultIntValidator)

    println(Validators[Int::class].validate(42))


}
