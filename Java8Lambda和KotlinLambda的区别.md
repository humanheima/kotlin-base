### Java8 Lambda 和 Kotlin Lambda的区别

Java8 Lambda：创建函数式接口匿名类对象的简化写法。本质上还是在创建一个匿名类对象。

在Java中使用

正常创建创建OnClickListener匿名类对象。

```java
view.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
                
    }
});
```

使用Lambda的简化写法

```java
view.setOnClickListener(v -> {
    
});
```

Kotlin Lambda：函数类型的对象。功能更多，写法更灵活。比如：

* lambda表达式作为函数参数。
* lambda表达式作为函数返回类型。
* 将一个lambda表达式赋值给一个变量，稍后使用。

一个有趣的现象

首先看一下在Kotlin里面使用在Java中定义的函数式接口

```kotlin
//完整写法
view.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View?) {
        println("Hello world")
    }
})

//使用lambda简化
view.setOnClickListener {
    //do something
}
```

然后当你在Kotlin中定义一个类似的接口的时候，那有意思的就来了。

```kotlin
interface KtOnClickListener {
    
    fun onClick(age: Int)
    
}

fun setKtOnClickListener(l: KtOnClickListener) {

}

```
使用

```kotlin
//注释1处
setKtOnClickListener(object : KtOnClickListener {
    override fun onClick(age: Int) {

    }
})

```

注释1处完整写法，并没有简化写法。

Kotlin1.4已经支持了函数式接口。

```kotlin
fun interface IntPredicate {
    fun accept(i: Int): Boolean
}

val isEven = IntPredicate { it % 2 == 0 }

fun main() { 
    println("Is 7 even? - ${isEven.accept(7)}")
}
```

这是因为Kotlin中其实已经不需要函数式接口了，直接用lambda表达式代替就行。所以Kotlin索性就不支持函数式接口了。但是当和Java交互的时候，Kotlin是支持这种用法的，当你的函数参数是Java的函数式接口的时候，依然可以使用Lambda来写参数。这可以理解为Kotlin对Java的特殊支持。



Kotlin中用lambda表达式代替函数式接口，如下所示：

```kotlin
//定义方法，参数使用lambda表达式
fun setKtOnClickListenerLambda(l: (age: Int) -> Unit) {

}
```

```kotlin
//使用
setKtOnClickListenerLambda { 
        
}
```

