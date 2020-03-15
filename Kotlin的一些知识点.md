### 如何判断一个了懒初始化的变量是否被初始化了？

自kotlin 1.2 版本开始可以检查
```kotlin
class Foo {

    private lateinit var lateInitVar: String

    fun initializationLogic() {
        println("isInitialized before assignment: " + this::lateInitVar.isInitialized)
        lateinitVar = "value"
        println("isInitialized after assignment: " + this::lateInitVar.isInitialized)
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

### map 和 flatMap的区别

```kotlin
public inline fun <T, R> Iterable<T>.map(transform: (T) -> R): List<R> {
    return mapTo(ArrayList<R>(collectionSizeOrDefault(10)), transform)
}

public inline fun <T, R> Iterable<T>.flatMap(transform: (T) -> Iterable<R>): List<R> {
    return flatMapTo(ArrayList<R>(), transform)
}

```
我们可以看到这两个方法都是Iterable的扩展函数。区别在于transform参数。

#### map

map方法的transform参数，是将集合中的每个元素转化为另一种类型。
```
transform: (T) -> R
```
例如将Int类型转化成String类型。如下所示：
```kotlin
 val list = listOf(1, 2, 3, 4)
 println(list.map { "Hello $it" })
```
输出结果
```
[Hello 1, Hello 2, Hello 3, Hello 4]
```

#### flatMap

map方法的transform参数，是将集合中的每个元素转化成一个Iterable。
```kotlin
transform: (T) -> Iterable<R>
```
例如我们有一个集合personList，每个人有不同的爱好爱好（hobby），我门想输出集合中每个人的兴趣爱好怎么办，这个时候可以使用flatMap将每个person转化成一个hobby集合。

```kotlin
fun main() {
 val dmw = Person(
        arrayListOf(
            Hobby("读书"),
            Hobby("跑步"),
            Hobby("足球"),
            Hobby("篮球")
        )
    )
    val caixukun = Person(
        arrayListOf(
            Hobby("唱"),
            Hobby("跳"),
            Hobby("rap"),
            Hobby("篮球")
        )
    )

    val personList = listOf(dmw, caixukun)

    val personHobbyList = personList.flatMap {
        it.hobbies
    }

    personHobbyList.forEach {
        println(it.hobbyType)
    }

}

class Person(var hobbies: List<Hobby>)

class Hobby(val hobbyType: String)

```



