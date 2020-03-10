
Kotlin let、apply、with的区别

### let

```kotlin
fun testLet(): Int {

    "Hello".let {
        println(it)
        println(it)
        println(it)
        return 1
    }
}
```

方法定义：可以看到let是一个扩展函数
```kotlin
/**
 * 调用指定的功能[block]，并将当前对象作为[block]的参数，返回[block]调用的结果。
 *
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R {
    //契约，暂且不管
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}

```

### apply

```kotlin
fun testApply() {
    val arrayList: ArrayList<String> = arrayListOf()
    arrayList.apply {
        add("Hello world")
        add("Hello world")
        add("Hello world")
    }.let {
        println(it)
    }
}
```

方法定义：可以看到apply是一个扩展函数
```kotlin
/**
 * 调用指定的功能[block]，并将当前对象作为[block]的接受者，返回当前对象。
 *
 */
@kotlin.internal.InlineOnly
public inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    //返回的是当前对象
    return this
}

```

### with

```kotlin
fun testWith() {
    val arrayList = arrayListOf<String>()
    with(arrayList) {
        add("Hello world")
        add("Hello world")
        add("Hello world")

    }
    println(arrayList)
    //输出[Hello world, Hello world, Hello world]
}

```

方法定义：
```kotlin
/**
 * 调用指定的功能[block]，将指定的[receiver]作为[block]的接受者，然后返回[block]的调用结果
 *
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return receiver.block()
}


```




