### 如何判断一个了懒初始化的变量是否被初始化了？

自kotlin 1.2 版本开始可以检查
```kotlin
class Foo {
    lateinit var lateinitVar: String

    fun initializationLogic() {
        println("isInitialized before assignment: " + this::lateinitVar.isInitialized)
        lateinitVar = "value"
        println("isInitialized after assignment: " + this::lateinitVar.isInitialized)
    }
}

fun main(args: Array<String>) {
	Foo().initializationLogic()
}
```
输出结果
```java
isInitialized before assignment: false
isInitialized after assignment: true
```

### Kotlin 类和方法默认都是final的。