[CSDN同步发布](https://blog.csdn.net/leilifengxingmw/article/details/103443753)

### 为什么需要协程？
协程可以简化异步编程，可以顺序地表达程序，协程也提供了一种避免阻塞线程并用更廉价、更可控的操作替代线程阻塞的方法 -- 挂起函数。

Kotlin 的协程是依靠编译器实现的, 并不需要操作系统和硬件的支持。编译器为了让开发者编写代码更简单方便, 提供了一些关键字(例如suspend), 并在内部自动生成了一些支持型的代码。

### 实现细节

#### Continuation passing style（CPS）
挂起函数铜通过Continuation passing style（CPS）来实现。当挂起函数被调用的时候，会有一个额外的`Continuation`参数传递给它。回想一下`await`挂起函数的声明如下所示：

```
suspend fun <T> CompletableFuture<T>.await(): T
```
但是，在通过CPS转换之后它的真实的方法声明如下所示：
```
fun <T> CompletableFuture<T>.await(continuation: Continuation<T>): Any?
```
我们看到类型参数`T`移动到了`Continuation`参数的类型参数（就是泛型参数）的位置上。方法实现的返回类型变成了`Any?`。为什么方法实现的返回类型要变成`Any?`呢？因为当挂起函数挂起协程的时候，挂起函数返回一个特殊的标记值`COROUTINE_SUSPENDED`(详情参考[coroutine intrinsics](https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md#coroutine-intrinsics)章节)。
当挂起函数没有挂起协程，继续协程执行的时候，挂起函数会立即返回执行结果或者抛出一个异常。这样，`await`方法返回类型`Any?`实际上是`COROUTINE_SUSPENDED`和`T`的并集。（类比Java，就好比一个方法既可以返回String类型又可以返回Integer类型，那么我们就声明这个方法返回Object）。

实际实现挂起函数的时候，不允许在它的栈帧中直接调用`continuation`，因为这有可能导致长时间运行的`协程`栈溢出。为什么呢？我的理解是因为如果一个协程很长的话，里面会有很多挂起点，那么每次从一个挂起点恢复的时候就会调用一次`resumeWith(Object result)`方法（每次方法调用对应一个栈帧），也就是说`resumeWith(Object result)`方法会被多次调用，是有可能导致栈溢出的。（类比Java，调用递归方法的时候就要注意栈溢出问题）。标准库中的`suspendCoroutine`函数通过跟踪`continuation`的调用向开发人员隐藏了这种复杂性，并且确保`continuation`无论以何种方式以及何时被调用，都与挂起函数的实际实现约定一致。

#### State machines（状态机）

高效实现协程是很关键的，例如尽可能使用更少的类和对象。很多语言使用状态机来实现协程，Kotlin也是使用状态机的方式来实现的。对于Kotlin，这种方式导致编译器为每个`包含挂起函数的lambda表达式`生成一个类，包含挂起函数的lambda表达式中可以有任意数量的挂起点。


核心思想：一个`包含挂起函数的lambda表达式`被编译成了一个状态机，状态对应挂起点，例如下面的这个`lambda表达式`体内有两个挂起点。
```
GlobalScope.launch {
    val a = a()
    val y = foo(a).await() // 挂起点1
    b()
    val z = bar(a, y).await() // 挂起点2
    c(z)
}
```
这个代码块有3个状态：
* 初始化（在任何挂起点之前，也就是上面代码块中的第一行）
* 在第一个挂起点之后
* 在第二个挂起点之后

每个状态都是都是一个`continuation`的入口。如下图所示。图片来自[Kotlin Coroutines(协程) 完全解析（二），深入理解协程的挂起、恢复与调度](https://www.jianshu.com/p/2979732fb6fb)


![协程continuation.jpg](https://upload-images.jianshu.io/upload_images/3611193-eb550071239aa8b4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上面的代码块被编译成一个匿名内部类，该类有一个方法来实现状态机，有一个成员变量来标志当前状态机的状态。该匿名类的Java伪代码如下所示：
```
class <anonymous_for_state_machine> extends SuspendLambda<...> {
    // 标志当前状态机的状态
    int label = 0
    
    // 协程的局部变量，就是上面在lambda表达式体内声明的变量
    A a = null
    Y y = null

    //该方法用来实现状态机
    void resumeWith(Object result) {
        if (label == 0) goto L0
        if (label == 1) goto L1
        if (label == 2) goto L2
        else throw IllegalStateException()
        
      L0:
        // result is expected to be `null` at this invocation
        a = a()
        label = 1
       //注释1处
        result = foo(a).await(this) // 'this' is passed as a continuation 
        //注释2处
        if (result == COROUTINE_SUSPENDED) return // return if await had suspended execution
      L1:
        // external code has resumed this coroutine passing the result of .await() 
        y = (Y) result
        b()
        label = 2
        result = bar(a, y).await(this) // 'this' is passed as a continuation
        if (result == COROUTINE_SUSPENDED) return // return if await had suspended execution
      L2:
        // external code has resumed this coroutine passing the result of .await()
        Z z = (Z) result
        c(z)
        label = -1 // No more steps are allowed
        return
    }          
}    
```
注释1处
```
 result = foo(a).await(this) // 'this' is passed as a continuation
```
把当前对象'this'作为一个`continuation`传递给挂起函数。

注释2处
```
if (result == COROUTINE_SUSPENDED) return // return if await had suspended execution
```
如果挂起函数`await`挂起了即返回了特殊的标记变量COROUTINE_SUSPENDED，就直接返回。

### 上面伪代码执行步骤

1. 当协程开始的时候，我们调用`resumeWith`，此时`label`是`0`，我们跳转到`L0`，然后执行一些操作，然后将`label`设置为下一个状态`1`，调用`.await()`并且当`.await()`方法挂起的时候直接`return`。

2. 当协程挂起恢复，继续执行的时候，我们再次调用`resumeWith`，此时跳转到`L1`，然后执行一些操作，然后将`label`设置为下一个状态`2`，调用`.await()`并且当`.await()`方法挂起的时候直接`return`。

3. 当协程挂起恢复，继续执行的时候，我们再次调用`resumeWith`，此时跳转到`L2`，将`label`设置为`-1`，也就是说协程执行结束。

### 循环中的挂起点
在一个循环中的挂起点只生成一个状态，因为循环也通过有条件的`goto`来工作。

```
var x = 0
while (x < 10) {
    x += nextNumber().await()
}
```
生成的伪代码
```
class <anonymous_for_state_machine> extends SuspendLambda<...> {
    // The current state of the state machine
    int label = 0
    
    // local variables of the coroutine
    int x
    
    void resumeWith(Object result) {
        if (label == 0) goto L0
        if (label == 1) goto L1
        else throw IllegalStateException()
        
      L0:
        x = 0
      LOOP:
       //注释1处
        if (x > 10) goto END
        label = 1
        result = nextNumber().await(this) // 'this' is passed as a continuation 
        if (result == COROUTINE_SUSPENDED) return 
      L1:
        // 注释2处
        x += ((Integer) result).intValue()
        label = -1
        goto LOOP
      END:
        label = -1 // No more steps are allowed
        return 
    }          
}    
```

在注释2处，每次累加结果后到会跳转到`LOOP`标签。

注释1处，判断当x>10就跳转到`END`，表示执行结束。

参考链接
* [Coroutines design document (KEEP)](https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md)`Implementation details`一节。
* [Kotlin Coroutines(协程) 完全解析（二），深入理解协程的挂起、恢复与调度](https://www.jianshu.com/p/2979732fb6fb)

