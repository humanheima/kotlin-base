在Kotlin中，lambda表达式会被正常的编译成匿名类。这表示每调用一次lambda表达式，一个额外的类就会被创建。并且如果lambda捕捉了某个变量，那么每次调用都会创建一个新的对象。这会带来运行时额外开销，导致使用lambda比使用一个直接执行相同代码的函数效率低。

举个例子：定义一个threadSafeMethod方法，该方法的action参数是一个lambda表达式

#### 没有内联的函数，lambda表达式没有捕捉变量
```kotlin
/**
 * 定义一个线程安全的方法
 */
fun <T> threadSafeMethod(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

fun main() {

    foo(ReentrantLock())
}

fun foo(l: Lock) {
    println("Before sync")
    threadSafeMethod(l) {
        println("Action")
    }
    println("After sync")
}

```
看一下反编译Kotlin字节码
```java
public final class Code8_13Kt {
   
   //注释1处
   public static final Object threadSafeMethod(Lock lock,  Function0 action) {
      lock.lock();

      Object var2;
      try {
         var2 = action.invoke();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public static final void foo(Lock l) {
      Intrinsics.checkParameterIsNotNull(l, "l");
      String var1 = "Before sync";
      System.out.println(var1);
      //注释2处
      threadSafeMethod(l, (Function0)null.INSTANCE);
      var1 = "After sync";
      System.out.println(var1);
   }
}

```
在注释1处，生成的threadSafeMethod方法的action参数lambda表达式被编译成了一个类Function0。
```kotlin
public interface Function0<out R> : Function<R> {
    /** Invokes the function. */
    public operator fun invoke(): R
}
```
注释2处，调用threadSafeMethod方法，需要传入一个Function0的实例，因为lambda表达式没有捕捉变量，所以这个实例是可以重复使用的。

### 没有内联的函数，lambda表达式捕捉变量

```kotlin
fun <T> threadSafeMethod(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}
fun main() {
    foo(ReentrantLock(), 1)
    foo(ReentrantLock(), 2)
    foo(ReentrantLock(), 3)
}

fun foo(l: Lock, number: Int) {

    threadSafeMethod(l) {
        println("Action one $number")
    }
}
```
查看反编译的Kotlin字节码
```java
public final class Code8_13Kt {
   public static final Object threadSafeMethod(Lock lock, Function0 action) {
      lock.lock();

      Object var2;
      try {
         var2 = action.invoke();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public static final void main() {
      foo((Lock)(new ReentrantLock()), 1);
      foo((Lock)(new ReentrantLock()), 2);
      foo((Lock)(new ReentrantLock()), 3);
   }

   public static final void foo(Lock l, final int number) {
      Intrinsics.checkParameterIsNotNull(l, "l");
      //注释1处，
      threadSafeMethod(l, (Function0)(new Function0() {
         public Object invoke() {
            this.invoke();
            return Unit.INSTANCE;
         }

         public final void invoke() {
            String var1 = "Action one " + number;
            boolean var2 = false;
            System.out.println(var1);
         }
      }));
   }
}
```
注释1处：每次调用threadSafeMethod方法的时候，都是new了一个Function0对象。

有没有可能让编译器生成跟Java语句同样高效的代码，但还能把重复的逻辑抽取到库函数中呢？是的，Kotlin编译器能够做到。如果使用`inline`修饰符标记一个函数，在函数被使用的时候编译器并不会生成函数调用的代码，而是使用函数实现的真实代码替换每一次的函数调用。

当一个函数被声明为inline时，它的函数体是内联的一一换句话说，函数体会被直接替换到函数被调用的地方，而不是被正常调用。我们把threadSafeMethod改为内联函数。


#### 内联的函数
```kotlin
inline fun <T> threadSafeMethod(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}
```
### 内联函数，lambda表达式没有捕捉变量

```kotlin
fun main() {

    foo(ReentrantLock())
}

fun foo(l: Lock) {
    println("Before sync")
    threadSafeMethod(l) {
        println("Action")
    }
    println("After sync")
}
```
看一下反编译Kotlin字节码

```java
public final class Code8_13Kt {
    
   public static final Object threadSafeMethod(Lock lock, Function0 action) {
      lock.lock();

      Object var3;
      try {
         var3 = action.invoke();
      } finally {
         InlineMarker.finallyStart(1);
         lock.unlock();
         InlineMarker.finallyEnd(1);
      }

      return var3;
   }

   public static final void foo(Lock l) {
      Intrinsics.checkParameterIsNotNull(l, "l");
      String var1 = "Before sync";
      System.out.println(var1);

      /****start*******/
      l.lock();

      try {
         String var3 = "Action";
         boolean var4 = false;
         System.out.println(var3);
         Unit var8 = Unit.INSTANCE;
      } finally {
         l.unlock();
      }
      
      /****end*******/

      var1 = "After sync";
      System.out.println(var1);
   }
}
```
看一下， 从注释start到end这段代码，
```java
    /****start*******/
    l.lock();

    try {
        var2 = false;
        String var3 = "Action";
        boolean var4 = false;
        System.out.println(var3);
        Unit var8 = Unit.INSTANCE;
    } finally {
         l.unlock();
    }
      
    /****end*******/

```

可以看到内联函数threadSafeMethod中的函数体和lambda表达式都被直接插入到了`foo`方法中。

### 内联函数，lambda表达式捕捉变量
```kotlin
fun main() {
    foo(ReentrantLock(), 1)
    foo(ReentrantLock(), 2)
    foo(ReentrantLock(), 3)
}

fun foo(l: Lock, number: Int) {

    threadSafeMethod(l) {
        println("Action one $number")
    }
}
```

看一下反编译Kotlin字节码
```java
public final class Code8_13Kt {
   public static final Object threadSafeMethod(Lock lock, Function0 action) {
      int $i$f$threadSafeMethod = 0;
      Intrinsics.checkParameterIsNotNull(lock, "lock");
      Intrinsics.checkParameterIsNotNull(action, "action");
      lock.lock();

      Object var3;
      try {
         var3 = action.invoke();
      } finally {
         InlineMarker.finallyStart(1);
         lock.unlock();
         InlineMarker.finallyEnd(1);
      }

      return var3;
   }

   public static final void main() {
      foo((Lock)(new ReentrantLock()), 1);
      foo((Lock)(new ReentrantLock()), 2);
      foo((Lock)(new ReentrantLock()), 3);
   }

   public static final void foo(Lock l, int number) {
      Intrinsics.checkParameterIsNotNull(l, "l");
      int $i$f$threadSafeMethod = false;
      l.lock();

      try {
         String var4 = "Action one " + number;
         System.out.println(var4);
      } finally {
         l.unlock();
      }
   }
}
```
我们看到内联函数threadSafeMethod中的函数体和lambda表达式都被直接插入到了foo方法中。

### lambda表达式和函数类型

在foo方法中，我们传入threadSafeMethod方法的action参数是一个lambda表达式
```kotlin
threadSafeMethod(l) {
    println("Action")
}
```
注意，在调用内联函数 时候也可以传递函数类型的变量作为参数：
```kotlin
fun fooTwo(l: Lock, body: () -> Unit) {
    println("Before sync")
    threadSafeMethod(l, body)
    println("After sync")
}
```
我们定义了fooTwo函数，在内部调用了threadSafeMethod方法，传入threadSafeMethod方法的action参数是一个函数类型。

看一下反编译Kotlin字节码
```java
public static final void fooTwo(@NotNull Lock l, @NotNull Function0 body) {
      String var2 = "Before sync";
      boolean var3 = false;
      System.out.println(var2);
      l.lock();

      try {
         Object var7 = body.invoke();
      } finally {
         l.unlock();
      }

      var2 = "After sync";
      var3 = false;
      System.out.println(var2);
}
```
我们可以看到threadSafeMethod方法的函数体被内联了，但是action参数没有被内联，因为此时还不知道传入的action参数到底是啥，没法内联。

### 内联函数多次替换

如果在两个不同的位置使用同个内联函数，但是用的是不同的 lambda表达式，那么内联函数会在每一个被调用的位置被分别内联。内联函数的代码会被拷贝到使用它
的两个不同位置，并把不同的 lambda替换到其中。
```kotlin
fun foo(l: Lock) {

    threadSafeMethod(l) {
        println("Action one")
    }

    threadSafeMethod(l) {
        println("Action two")
    }
}
```

```java
public static final void foo(@NotNull Lock l) {
      l.lock();

      String var3;
      try {
         //第一次替换
         var3 = "Action one";
         System.out.println(var3);
      } finally {
         l.unlock();
      }

      $i$f$threadSafeMethod = false;
      l.lock();

      try {
         //第二次替换
         var3 = "Action two";
         System.out.println(var3);
      } finally {
         l.unlock();
      }
}
```
**注意：这也意味着，作为内联函数不宜过长，不然，每次都替换，会导致代码量大量增加。**

### 内联函数的限制

鉴于内联的运作方式，不是所有使用lambda的函数都可以被内联。当函数被内联的时候，作为参数的 =lambda 表达式的函数体会被直接替换到最终生成的代码中这将限制lambda参数的使用，如果lambda参数被直接调用，这样的代码能被容易地内联。但如果lambda参数在某个地方被保存起来，以便后面可以继续使用，lambda表达式的代码将不能被内联因为必须要有一个包含这些代码的对象存在。

一般来说，lambda表达式如果被直接调用或者作为参数传递给另外inline函数，它是可以被内联的。否则，编译器会禁止参数被内联并给出错误信息“Illegal usage of inline-parameter ”（非法使用内联参数）。

如果一个函数期望两个或更多 lambda 参数 可以选择只内联其中一些参数。这是有道理的，因为一个lambda可能会包含很多代码或者不允许内联的方式使用。接收这样的非内联lambda的参数，可以用 noinline 修饰符来标记它。
```kotlin
inline fun foo(inlined: () -> Unit, noinline notinlined: () -> Unit) { 

}
```

总结：
对于普通的函数调用，JVM己经提供了强大的内联支持。它会分析代码的执行，并在任何通过内联能够带来好处的时候将函数调用内联。这是在将宇节码转换成机器代码时自动完成的。在字节码中，每一个函数的实现只会出现一次，并不需要跟Kotlin的内联函数一样，每个调用的地方都拷贝一次。再说，如果函数被直接调用，调用栈会更加清晰。

将带有lambda参数的函数内联能带来好处。首先，通过内联避免的运行时开销更明显了。不仅节约了函数调用的开销，而且节约了为lambda创建匿名类，以及创建lambda实例对象的开销。

在使用inline关键字的时候，你还是应该注意代码的长度。如果你要内联的函数很大，将它的字节码拷贝到每一个调用点将会极大地增加字节码的长度。在这种情况下，你应该将那些与lambda参数无关的代码抽取到一个独立的非内联函数中。

部分摘抄自《Kotlin实战》




