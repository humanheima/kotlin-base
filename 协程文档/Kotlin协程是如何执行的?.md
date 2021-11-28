最简单的一段协程代码

```kotlin
fun main() {
    GlobalScope.launch {
        println("Hello world! GlobalScope current Thread ${Thread.currentThread().name}")
        //第一个挂起点
        delay(100)
        println("Hello world! GlobalScope first after delay current Thread ${Thread.currentThread().name}")
        //第二个挂起点
        delay(200)
        println("Hello world! GlobalScope second after delay current Thread ${Thread.currentThread().name}")
    }
    Thread.sleep(2000)
    println("Hello world! current Thread  ${Thread.currentThread().name}")
}
```

输出结果

```
Hello world! GlobalScope current Thread DefaultDispatcher-worker-1
Hello world! GlobalScope first after delay current Thread DefaultDispatcher-worker-1
Hello world! GlobalScope second after delay current Thread DefaultDispatcher-worker-1
Hello world! current Thread  main

```

将Kotlin字节码反编译成Java代码

```java
package learncoroutine;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.GlobalScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 1, 18},
   bv = {1, 0, 3},
   k = 2,
   d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u001a\u0006\u0010\u0000\u001a\u00020\u0001¨\u0006\u0002"},
   d2 = {"main", "", "kotlin-base"}
)
public final class TestCoroutinesTheoryKt {
   public static final void main() {
      BuildersKt.launch$default((CoroutineScope)GlobalScope.INSTANCE, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2((Continuation)null) {
         int label;

         @Nullable
         public final Object invokeSuspend(@NotNull Object $result) {
            StringBuilder var10000;
            Thread var10001;
            String var2;
            boolean var3;
            label17: {
               Object var4 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch(this.label) {
               case 0:
                  ResultKt.throwOnFailure($result);
                  var10000 = (new StringBuilder()).append("Hello world! GlobalScope current Thread ");
                  var10001 = Thread.currentThread();
                  Intrinsics.checkExpressionValueIsNotNull(var10001, "Thread.currentThread()");
                  var2 = var10000.append(var10001.getName()).toString();
                  var3 = false;
                  System.out.println(var2);
                  this.label = 1;
                  if (DelayKt.delay(100L, this) == var4) {
                     return var4;
                  }
                  break;
               case 1:
                  ResultKt.throwOnFailure($result);
                  break;
               case 2:
                  ResultKt.throwOnFailure($result);
                  break label17;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               var10000 = (new StringBuilder()).append("Hello world! GlobalScope first after delay current Thread ");
               var10001 = Thread.currentThread();
               Intrinsics.checkExpressionValueIsNotNull(var10001, "Thread.currentThread()");
               var2 = var10000.append(var10001.getName()).toString();
               var3 = false;
               System.out.println(var2);
               this.label = 2;
               if (DelayKt.delay(200L, this) == var4) {
                  return var4;
               }
            }

            var10000 = (new StringBuilder()).append("Hello world! GlobalScope second after delay current Thread ");
            var10001 = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(var10001, "Thread.currentThread()");
            var2 = var10000.append(var10001.getName()).toString();
            var3 = false;
            System.out.println(var2);
            return Unit.INSTANCE;
         }

         @NotNull
         public final Continuation create(@Nullable Object value, @NotNull Continuation completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            Function2 var3 = new <anonymous constructor>(completion);
            return var3;
         }

         public final Object invoke(Object var1, Object var2) {
            return ((<undefinedtype>)this.create(var1, (Continuation)var2)).invokeSuspend(Unit.INSTANCE);
         }
      }), 3, (Object)null);
      Thread.sleep(2000L);
      StringBuilder var10000 = (new StringBuilder()).append("Hello world! current Thread  ");
      Thread var10001 = Thread.currentThread();
      Intrinsics.checkExpressionValueIsNotNull(var10001, "Thread.currentThread()");
      String var0 = var10000.append(var10001.getName()).toString();
      boolean var1 = false;
      System.out.println(var0);
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }
}
```

我们反编译生成的`Function2`这个参数，对应的就是协程中的代码块：
```
{
    println("Hello world! GlobalScope current Thread ${Thread.currentThread().name}")
    //第一个挂起点
    delay(100)
    println("Hello world! GlobalScope first after delay current Thread ${Thread.currentThread().name}")
    //第二个挂起点
    delay(200)
    println("Hello world! GlobalScope second after delay current Thread ${Thread.currentThread().name}")
}
```

BuildersKt这个类，其实就是`Builders.common.kt`文件编译成的类。

```kotlin
@file:JvmMultifileClass
@file:JvmName("BuildersKt")

package kotlinx.coroutines
```


CoroutineScope.launch方法是在`Builders.common.kt`文件中声明的。

```kotlin
public fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val newContext = newCoroutineContext(context)
    //注释1处
    val coroutine = if (start.isLazy)
        LazyStandaloneCoroutine(newContext, block) else
        StandaloneCoroutine(newContext, active = true)
    //注释2处
    coroutine.start(start, coroutine, block)
    return coroutine
}
```
注释1处，默认使用`StandaloneCoroutine`。构造函数中传递了`parentContext`和`active`两个参数。

```kotlin
private open class StandaloneCoroutine(
    parentContext: CoroutineContext,
    active: Boolean
) : AbstractCoroutine<Unit>(parentContext, active) {
    override fun handleJobException(exception: Throwable): Boolean {
        handleCoroutineException(context, exception)
        return true
    }
}
```

注释2处，启动协程。接下来我们看一下

AbstractCoroutine类中定义的 start 方法

```kotlin
/**
 * 使用指定的代码块 [block] 和 启动策略 [start] 来启动协程。
 * 在该协程上最多调用一次这个方法。
 *
 * 第一，这个方法使用传递给该协程的`parentContext`来初始化父job。
 * 第二，根据启动策略来启动该协程。
 * 
 * 1. 启动策略是 [DEFAULT] 使用 [startCoroutineCancellable].
 * 2. 启动策略是[ATOMIC]使用 [startCoroutine].
 * 3. [UNDISPATCHED] 使用 [startCoroutineUndispatched].
 * 4. [LAZY] does nothing.
 */
public fun <R> start(start: CoroutineStart, receiver: R, block: suspend R.() -> T) {
    //注释1处
    initParentJob()
    //注释2处
    start(block, receiver, this)
}
```
注释1处，初始化父Job。里面会将协程与父协程关联，这样父协程才会等待所有子协程结束以后自己才结束，以及子协程出现异常后，异常传播到符协程等等。这不是本文的重点，可以暂时忽略。

注释2处，这里注意一下，这里的`start(block, receiver, this)`是调用的`CoroutineStart`中的`invoke`方法并不是`AbstractCoroutine`类中定义的`start`方法。

CoroutineStart是一个枚举类，枚举值有以下几个。


* DEFAULT： -- 根据上下文立即调度执行协程

* LAZY：-- 启动一个惰性协程

* ATOMIC：-- 根据上下文自动调度执行协程，和DEFAULT类似，但是这种类型的协程在开始执行之前不能取消

* UNDISPATCHED： -- 在当前线程立即执行协程知道第一个挂起点。协程从第一个挂起点恢复以后是在哪个线程是不确定的。


CoroutineStart类的 invoke 方法

传入的receiver参数是StandaloneCoroutine，传入的completion参数也是一个StandaloneCoroutine对象。这两个参数其实是同一个对象。

```kotlin
@InternalCoroutinesApi
public operator fun <R, T> invoke(block: suspend R.() -> T, receiver: R, completion: Continuation<T>): Unit =
    when (this) {
        //注释1处，这里的receiver就是StandaloneCoroutine
        DEFAULT -> block.startCoroutineCancellable(receiver, completion)
        ATOMIC -> block.startCoroutine(receiver, completion)
        UNDISPATCHED -> block.startCoroutineUndispatched(receiver, completion)
        LAZY -> Unit // will start lazily
}
```

我们使用的CoroutineStart是DEFAULT，所以会走到注释1处，调用的是在`Cancellable.kt`文件中声明的方法。

```kotlin
/**
 * 使用该方法以可取消的方式来启动协程，当协程在等待调度的时候可以被取消。
 */
internal fun <R, T> (suspend (R) -> T).startCoroutineCancellable(receiver: R, completion: Continuation<T>) =
    runSafely(completion) {
        createCoroutineUnintercepted(receiver, completion).intercepted().resumeCancellableWith(Result.success(Unit))
    }
```

首先调用的是`runSafely`方法来执行指定的代码块并且当协程出现异常的时候完成，就是调用StandaloneCoroutine的resumeWith方法。

```kotlin
private inline fun runSafely(completion: Continuation<*>, block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        //出现异常的时候完成，completion就是我们最初的协程代码块
        completion.resumeWith(Result.failure(e))
    }
}
```

接下来我们看一看传递给runSafely的代码块。首先是`createCoroutineUnintercepted(receiver, completion)`，这个方法是在`IntrinsicsJvm.kt`中实现的。

```kotlin
/**
 * 返回不可不可拦截接收类型为R，返回类型为T的continuation。
 */
@SinceKotlin("1.3")
public actual fun <R, T> (suspend R.() -> T).createCoroutineUnintercepted(
    receiver: R,
    completion: Continuation<T>
): Continuation<Unit> {
    //probeCoroutineCreated方法内部直接返回completion，没有做什么操作
    val probeCompletion = probeCoroutineCreated(completion)
    return if (this is BaseContinuationImpl)
        //注释1处
        create(receiver, probeCompletion)
    else {
        createCoroutineFromSuspendFunction(probeCompletion) {
            (this as Function2<R, Continuation<T>, Any?>).invoke(receiver, it)
        }
    }
}
```

注释1处：通过debug，`this`是一个函数类型`suspend R.() -> T`，其实就是我们的协程代码块。这里的接受者类型R就是上文的`StandaloneCoroutine`。
然后这里的create()方法，这里猜测调用反编译出来的匿名内部类Function2的create()方法。

```kotlin
@NotNull
public final Continuation create(@Nullable Object value, Continuation completion) {
    Intrinsics.checkParameterIsNotNull(completion, "completion");
    //传入Function2构造函数的第一个参数为null，第二个参数是Continuation，其实就是上文的StandaloneCoroutine对象。
    Function2 var3 = new <anonymous constructor>(completion);
    return var3;
}
```

create方法，最终返回的对象是Continuation类型，通过debug，就是反编译出来的Function2的i`nvokeSuspend`方法返回的对象。

`Continuation at learncoroutine.TestCoroutinesTheoryKt$main$1.invokeSuspend(TestCoroutinesTheory.kt)`

再把上面的代码贴一下。

```
createCoroutineUnintercepted(completion).intercepted().resumeCancellableWith(Result.success(Unit))


```

`createCoroutineUnintercepted(completion)`这一步走完了，下面看`intercepted()`这一步。

这个方法是定义在`IntrinsicsJvm.kt`文件中的Continuation的扩展函数。

```kotlin
@SinceKotlin("1.3")
public actual fun <T> Continuation<T>.intercepted(): Continuation<T> =
    (this as? ContinuationImpl)?.intercepted() ?: this
```

通过debug发现`(this as? ContinuationImpl)`是条件是满足的，也就是说我们反编译出来的代码中`invokeSuspend`方法返回的对象可以看做是一个ContinuationImpl。那么接下来我们看一下ContinuationImpl的intercepted方法。

ContinuationImpl的intercepted方法。

```kotlin
public fun intercepted(): Continuation<Any?> =
        intercepted
            ?: (context[ContinuationInterceptor]?.interceptContinuation(this) ?: this)
                .also { intercepted = it }
```

`context[ContinuationInterceptor]`返回的就是`Dispatchers.Default`对象，调用的`interceptContinuation`方法是在CoroutineDispatcher类中声明的。

```kotlin
/**
 * 返回一个 continuation 包裹传入的 [continuation]，从而拦截所有的协程恢复操作。
 */
 public final override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        DispatchedContinuation(this, continuation)
```

构建了一个DispatchedContinuation对象。

```kotlin
internal class DispatchedContinuation<in T>(
    @JvmField val dispatcher: CoroutineDispatcher,
    @JvmField val continuation: Continuation<T>
) : DispatchedTask<T>(MODE_ATOMIC_DEFAULT), CoroutineStackFrame, Continuation<T> by continuation {
    //...
 }
```
传入的dispatcher参数是`Dispatchers.Default`，continuation参数是反编译出来的代码中invokeSuspend方法返回的对象，可以看做一个ContinuationImpl对象。

DispatchedContinuation实现了Continuation并委托给传入的continuation参数（一个ContinuationImpl对象）。

然后调用DispatchedContinuation的resumeCancellableWith方法。

```kotlin
@InternalCoroutinesApi
public fun <T> Continuation<T>.resumeCancellableWith(result: Result<T>): Unit = when (this) {
    //注释1处，条件满足
    is DispatchedContinuation -> resumeCancellableWith(result)
    else -> resumeWith(result)
}
```
注释1处，条件满足调用DispatchedContinuation的resumeCancellableWith方法。

```kotlin
inline fun resumeCancellableWith(result: Result<T>) {
    val state = result.toState()
    if (dispatcher.isDispatchNeeded(context)) {
        _state = state
        //调度是可取消的
        resumeMode = MODE_CANCELLABLE
        //注释1处，调度器开始调度
        dispatcher.dispatch(context, this)
    } else {
        executeUnconfined(state, MODE_CANCELLABLE) {
            if (!resumeCancelled()) {
                resumeUndispatchedWith(result)
            }
        }
    }
}
```

默认的调度器是`Dispatchers.Default`，默认是一个`DefaultScheduler`对象。注释1处，调度器开始调度。我们注意一下，dispatch方法的第二个参数是一个`Runnable`对象。我们可以猜测，当Runnable被调度的时候肯定会调用Runnable的run方法。

dispatch方法是在CoroutineDispatcher类中声明的，ExperimentalCoroutineDispatcher类实现了dispatch方法。

```kotlin
override fun dispatch(context: CoroutineContext, block: Runnable): Unit =
        try {
            //注释1处
            coroutineScheduler.dispatch(block)
        } catch (e: RejectedExecutionException) {
            DefaultExecutor.dispatch(context, block)
        }
```
注释1处，使用`coroutineScheduler`来调度任务。这个变量也是在ExperimentalCoroutineDispatcher类中声明的。

```kotlin
private var coroutineScheduler = createScheduler()

private fun createScheduler() = CoroutineScheduler(corePoolSize, maxPoolSize, 
    idleWorkerKeepAliveNs, schedulerName)
```
注意一下，CoroutineScheduler像不像线程池，像极了，哈哈。

CoroutineScheduler的dispatch方法

```kotlin
fun dispatch(block: Runnable, taskContext: TaskContext = NonBlockingContext, tailDispatch: Boolean = false) {
    trackTask() // this is needed for virtual time support
    //将传入的Runnable封装成一个Task   
    val task = createTask(block, taskContext)
    // 尝试将Task加入到本地队列，加入不成功则尝试加入到全局队列。
    val currentWorker = currentWorker()
    val notAdded = currentWorker.submitToLocalQueue(task, tailDispatch)
   //notAdded不为null表示加入到本地队列失败
    if (notAdded != null) {
        if (!addToGlobalQueue(notAdded)) {
            // 无法加入到全局队列，抛出异常
            throw RejectedExecutionException("$schedulerName was terminated")
        }
    }
    val skipUnpark = tailDispatch && currentWorker != null
    // Checking 'task' instead of 'notAdded' is completely okay
    if (task.mode == TASK_NON_BLOCKING) {
        if (skipUnpark) return
        //注释1处，通知cpu干活
        signalCpuWork()
    } else {
        // Increment blocking tasks anyway
        signalBlockingWork(skipUnpark = skipUnpark)
    }
}
```
方法的大概逻辑就是将传入的Runnable封装成一个Task，然后加入到队列。然后唤醒Worker工作，或者创建新的Worker开始工作。

注释1处 signalCpuWork方法

```kotlin
internal fun signalCpuWork() {
    if (tryUnpark()) return
    if (tryCreateWorker()) return
    tryUnpark()
}
```
这个方法大致的步骤就是：
1. 尝试唤醒现有的Worker进行工作。如果成功就返回，Worker会从队列中取任务来执行。
2. 第一步返回fasle，则创建新的Worker，创建成功会启动Worker。如果创建成功就直接返回。
3. 第二步返回fasle继续尝试唤醒已有的Worker。

我们看一下第二步的过程

```kotlin
private fun tryCreateWorker(state: Long = controlState.value): Boolean {
    val created = createdWorkers(state)
    val blocking = blockingTasks(state)
    val cpuWorkers = (created - blocking).coerceAtLeast(0)
    if (cpuWorkers < corePoolSize) {
        val newCpuWorkers = createNewWorker()
        //注释1处，创建新的Worker
        if (newCpuWorkers == 1 && corePoolSize > 1) createNewWorker()
        if (newCpuWorkers > 0) return true
    }
    return false
}
```

```kotlin
private fun createNewWorker(): Int {
    synchronized(workers) {
       if (isTerminated) return -1
       //...
       val worker = Worker(newIndex)//创建Worker
       workers[newIndex] = worker
       require(newIndex == incrementCreatedWorkers())
       worker.start()//启动Worker
       return cpuWorkers + 1
    }
}
```

接下来我们看一看Worker这个类，继承了Thread类并重写了run方法。

```kotlin
internal inner class Worker private constructor() : Thread() {

    override fun run() = runWorker()

}
```

Worker继承了Thread，当线程运行的时候会调用`runWorker`方法。

runWorker方法的精简版

```kotlin
private fun runWorker() {
    while (!isTerminated && state != WorkerState.TERMINATED) {
       //findTask方法是阻塞的，如果没有任务会阻塞
        val task = findTask(mayHaveLocalTasks)
            // Task found. Execute and repeat
            if (task != null) {
                rescanned = false
                //注释1处，执行任务
                executeTask(task)
                continue
            } 
        }
        tryReleaseCpu(WorkerState.TERMINATED)
}
```
runWorker方法的逻辑可以简单看做就是循环取任务来执行，取任务是阻塞的，并在适当的时候退出。

注释1处执行任务。

```kotlin
private fun executeTask(task: Task) {
    val taskMode = task.mode
    idleReset(taskMode)
    beforeTask(taskMode)
    runSafely(task)//执行任务
   afterTask(taskMode)
}
```

```kotlin
fun runSafely(task: Task) {
    try {
        //Runnable运行
        task.run()
    } catch (e: Throwable) {
        val thread = Thread.currentThread()
        thread.uncaughtExceptionHandler.uncaughtException(thread, e)
    } finally {
        unTrackTask()
    }
}
```

通过上面分析我们知道运行的Runnable是一个DispatchedContinuation对象。

DispatchedContinuation类继承了DispatchedTask，DispatchedTask重写了Runnable的run方法。

```kotlin
public final override fun run() {
    val taskContext = this.taskContext
    var fatalException: Throwable? = null
    try {
       //DispatchedContinuation重写了获取delegate的方法，返回一个DispatchedContinuation对象
        val delegate = delegate as DispatchedContinuation<T>
        //注释1处
        val continuation = delegate.continuation
        val context = continuation.context
        val state = takeState() // NOTE: Must take state in any case, even if cancelled
        //使用指定的上下文执行协程
        withCoroutineContext(context, delegate.countOrElement) {
            val exception = getExceptionalResult(state)
            //是可取消的模式 MODE_CANCELLABLE
            val job = if (resumeMode.isCancellableMode) context[Job] else null
            if (exception == null && job != null && !job.isActive) {
                val cause = job.getCancellationException()
                cancelResult(state, cause)
                continuation.resumeWithStackTrace(cause)
            } else {
                if (exception != null) continuation.resumeWithException(exception)
                //注释2处，正常调用resume方法
                else continuation.resume(getSuccessfulResult(state))
            }
        }
    } catch (e: Throwable) {
        // This instead of runCatching to have nicer stacktrace and debug experience
        fatalException = e
    } finally {
        val result = runCatching { taskContext.afterTask() }
        handleFatalException(fatalException, result.exceptionOrNull())
    }
}
```
注释1处，获取DispatchedContinuation中的continuation实例，就是反编译出来的代码中invokeSuspend方法返回的对象，可以看做一个ContinuationImpl对象。

注释2处，正常调用resume方法
```kotlin
public inline fun <T> Continuation<T>.resume(value: T): Unit =
    resumeWith(Result.success(value))
```
然后调用`ContinuationImpl`的resumeWith方法。这个方法在BaseContinuationImpl类中就实现了。

```kotlin
public final override fun resumeWith(result: Result<Any?>) {
    // This loop unrolls recursion in current.resumeWith(param) to make saner and shorter stack traces on resume
    var current = this
    var param = result
    while (true) {
        // Invoke "resume" debug probe on every resumed continuation, so that a debugging library infrastructure
        // can precisely track what part of suspended callstack was already resumed
        probeCoroutineResumed(current)
        with(current) {
            //这个completion通过debug，是我们传入的StandaloneCoroutine对象
            val completion = completion!! // fail fast when trying to resume continuation without completion
            val outcome: Result<Any?> =
                try {
                    //注释1处，这里的invokeSuspend就是我们反编译出来的Function2对象的invokeSuspend方法。如果是协程挂起了立即返回
                    val outcome = invokeSuspend(param)
                    if (outcome === COROUTINE_SUSPENDED) return
                    //注释2处，有计算结果了
                    Result.success(outcome)
                } catch (exception: Throwable) {
                    Result.failure(exception)
                }
            releaseIntercepted() // this state machine instance is terminating
            if (completion is BaseContinuationImpl) {
                // unrolling recursion via loop
                current = completion
                param = outcome
            } else {
                //注释3处，最终执行完毕
                completion.resumeWith(outcome)
                return
            }
        }
    }
}
```

注释1处，这里的invokeSuspend就是我们反编译出来的Function2对象的invokeSuspend方法。这时候具体的调用是：


```
println("Hello world! GlobalScope current Thread ${Thread.currentThread().name}")
//第一个挂起点
delay(100)
```

执行到`delay(100)`的时候，协程挂起了，直接return。这时候第一阶段执行完毕。


但是整个协程还没有执行完毕，那么delay之后又是怎么恢复的呢，继续向下看。


```kotlin 
public suspend fun delay(timeMillis: Long) {
    if (timeMillis <= 0) return // don't delay
    return suspendCancellableCoroutine sc@ { cont: CancellableContinuation<Unit> ->
        //cont是一个CancellableContinuationImpl类型的对象，就是我们 反编译出来的invokeSuspend那段代码。
        cont.context.delay.scheduleResumeAfterDelay(timeMillis, cont)
    }
}
```

通过debug`cont.context.delay`是`DefaultExecutor`。看一下DefaultExecutor的`scheduleResumeAfterDelay`方法。


```kotlin
internal actual object DefaultExecutor : EventLoopImplBase(), Runnable {

    //...

}
```

`scheduleResumeAfterDelay`方法在父类EventLoopImplBase中。文件是`EventLoop.common.kt`

```kotlin
public override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
    val timeNanos = delayToNanos(timeMillis)
    if (timeNanos < MAX_DELAY_NS) {
        val now = nanoTime()
        DelayedResumeTask(now + timeNanos, continuation).also { task ->
            continuation.disposeOnCancellation(task)
            //注释1处，EventLoopImplBase的schedule方法。
            schedule(now, task)
        }
    }
}
```

EventLoopImplBase的schedule方法。

```kotlin
public fun schedule(now: Long, delayedTask: DelayedTask) {
    //注释1处，将任务加入队列
    when (scheduleImpl(now, delayedTask)) {
        //注释2处，如果当前延迟队列的第一个任务就是刚添加的任务，调用unpark()方法，唤醒线程进行工作
        SCHEDULE_OK -> if (shouldUnpark(delayedTask)) unpark()
        SCHEDULE_COMPLETED -> reschedule(now, delayedTask)
        SCHEDULE_DISPOSED -> {} // do nothing -- task was already disposed
        else -> error("unexpected result")
    }
}
```
注释1处，将任务加入队列。

注释2处，如果当前延迟队列的第一个任务就是刚添加的任务，调用unpark()方法，唤醒线程进行工作。唤醒的线程到底是谁呢？就是`DefaultExecutor`所在的线程。

```kotlin
internal actual object DefaultExecutor : EventLoopImplBase(), Runnable {
    //...

    override fun run() {
        ThreadLocalEventLoop.setEventLoop(this)
        registerTimeLoopThread()
        try {
            var shutdownNanos = Long.MAX_VALUE
            if (!notifyStartup()) return
            //注释1处，
            while (true) {
                Thread.interrupted() // just reset interruption flag
                //从延迟任务队列中取下一个任务要执行的时刻
                var parkNanos = processNextEvent()
                if (parkNanos == Long.MAX_VALUE) {
                    // 没有任务，在指定的时间内停止线程。nothing to do, initialize shutdown timeout
                    val now = nanoTime()
                    if (shutdownNanos == Long.MAX_VALUE) shutdownNanos = now + KEEP_ALIVE_NANOS
                    val tillShutdown = shutdownNanos - now
                    if (tillShutdown <= 0) return // shut thread down
                    parkNanos = parkNanos.coerceAtMost(tillShutdown)
                } else
                    shutdownNanos = Long.MAX_VALUE
                if (parkNanos > 0) {
                    // check if shutdown was requested and bail out in this case
                    if (isShutdownRequested) return
                    //注释1处
                    parkNanos(this, parkNanos)
                }
            }
        } finally {
            _thread = null // this thread is dead
            acknowledgeShutdownIfNeeded()
            unregisterTimeLoopThread()
            // recheck if queues are empty after _thread reference was set to null (!!!)
            if (!isEmpty) thread // recreate thread if it is needed
        }
    }

}

```

注释1，当有任务，但是还没到任务的执行事件，当前线程就阻塞一定的时间`parkNanos`。然后当一下情况发生时，当前线程会被唤醒。

1. 其他线程调用`unpark(Thread thread)`，唤醒当前线程。
2. 其他线程中断了当前线程。 {@linkplain Thread#interrupt interrupts}。
3. 等待时间已到。

当线程正常被唤醒的时候，会继续执行任务。在这里就是执行run方法中的死循环。

具体是怎么调度的我们暂时不关注，当到了指定的延迟，肯定会调用DelayedResumeTask的run方法。

```kotlin
private inner class DelayedResumeTask(
        nanoTime: Long,
        private val cont: CancellableContinuation<Unit>
) : DelayedTask(nanoTime) {
    override fun run() { 
        //注释1处，cont是一个CancellableContinuationImpl类型的对象。
        with(cont) { resumeUndispatched(Unit) } 
}
    override fun toString(): String = super.toString() + cont.toString()
}
```

cont是一个CancellableContinuationImpl类型的对象。

CancellableContinuationImpl的resumeUndispatched方法。


```kotlin
override fun CoroutineDispatcher.resumeUndispatched(value: T) {
    val dc = delegate as? DispatchedContinuation
    //注释1处，CancellableContinuationImpl的resumeImpl方法。
    resumeImpl(value, if (dc?.dispatcher === this) MODE_UNDISPATCHED else resumeMode)
}

```

注释1处，CancellableContinuationImpl的resumeImpl方法。

```kotlin
private fun resumeImpl(proposedUpdate: Any?, resumeMode: Int): CancelledContinuation? {
    _state.loop { state ->
        when (state) {
            is NotCompleted -> {
                if (!_state.compareAndSet(state, proposedUpdate)) return@loop // retry on cas failure
                detachChildIfNonResuable()
                //注释1处，
                dispatchResume(resumeMode)
                return null
            }
            is CancelledContinuation -> {
                /*
                 * If continuation was cancelled, then resume attempt must be ignored,
                 * because cancellation is asynchronous and may race with resume.
                 * Racy exceptions will be lost, too.
                 */
                if (state.makeResumed()) return state // tried to resume just once, but was cancelled
            }
        }
        alreadyResumedError(proposedUpdate) // otherwise -- an error (second resume attempt)
    }
}

```

```kotlin
private fun dispatchResume(mode: Int) {
    if (tryResume()) return // completed before getResult invocation -- bail out
    // otherwise, getResult has already commenced, i.e. completed later or in other thread
    //注释1处，调用DispatchedTask的扩展方法dispatch。
    dispatch(mode)
}
```


```kotlin
internal fun <T> DispatchedTask<T>.dispatch(mode: Int) {
    val delegate = this.delegate
    if (mode.isDispatchedMode && delegate is DispatchedContinuation<*> && mode.isCancellableMode == resumeMode.isCancellableMode) {
        // dispatch directly using this instance's Runnable implementation
        val dispatcher = delegate.dispatcher
        val context = delegate.context
        if (dispatcher.isDispatchNeeded(context)) {
            //注释1处，dispatcher继续调度
            dispatcher.dispatch(context, this)
        } else {
            resumeUnconfined()
        }
    } else {
        resume(delegate, mode)
    }
}

```

注释1处，dispatcher继续调度，就会回到我们上面分析过的ExperimentalCoroutineDispatcher。然后回到DispatchedTask。最终执行的是DispatchedTask重写了Runnable的run方法。

```kotlin
public final override fun run() {
    val taskContext = this.taskContext
    var fatalException: Throwable? = null
    try {
       //DispatchedContinuation重写了获取delegate的方法，返回一个DispatchedContinuation对象
        val delegate = delegate as DispatchedContinuation<T>
        //注释1处
        val continuation = delegate.continuation
        val context = continuation.context
        val state = takeState() // NOTE: Must take state in any case, even if cancelled
        //使用指定的上下文执行协程
        withCoroutineContext(context, delegate.countOrElement) {
            val exception = getExceptionalResult(state)
            //是可取消的模式 MODE_CANCELLABLE
            val job = if (resumeMode.isCancellableMode) context[Job] else null
            if (exception == null && job != null && !job.isActive) {
                val cause = job.getCancellationException()
                cancelResult(state, cause)
                continuation.resumeWithStackTrace(cause)
            } else {
                if (exception != null) continuation.resumeWithException(exception)
                //注释2处，正常调用resume方法
                else continuation.resume(getSuccessfulResult(state))
            }
        }
    } catch (e: Throwable) {
        // This instead of runCatching to have nicer stacktrace and debug experience
        fatalException = e
    } finally {
        val result = runCatching { taskContext.afterTask() }
        handleFatalException(fatalException, result.exceptionOrNull())
    }
}
```

注释1处，获取DispatchedContinuation中的continuation实例，就是反编译出来的代码中invokeSuspend方法返回的对象，可以看做一个ContinuationImpl对象。

注释2处，正常调用resume方法。

```kotlin
public inline fun <T> Continuation<T>.resume(value: T): Unit =
    resumeWith(Result.success(value))
```

然后调用`ContinuationImpl`的resumeWith方法。这个方法在BaseContinuationImpl类中就实现了。

```kotlin
public final override fun resumeWith(result: Result<Any?>) {
    // This loop unrolls recursion in current.resumeWith(param) to make saner and shorter stack traces on resume
    var current = this
    var param = result
    while (true) {
        // Invoke "resume" debug probe on every resumed continuation, so that a debugging library infrastructure
        // can precisely track what part of suspended callstack was already resumed
        probeCoroutineResumed(current)
        with(current) {
            //这个completion通过debug，是我们传入的StandaloneCoroutine对象
            val completion = completion!! // fail fast when trying to resume continuation without completion
            val outcome: Result<Any?> =
                try {
                    //注释1处，这里的invokeSuspend就是我们反编译出来的Function2对象的invokeSuspend方法。如果是协程挂起了立即返回
                    val outcome = invokeSuspend(param)
                    if (outcome === COROUTINE_SUSPENDED) return
                    //注释2处，有计算结果了
                    Result.success(outcome)
                } catch (exception: Throwable) {
                    Result.failure(exception)
                }
            releaseIntercepted() // this state machine instance is terminating
            if (completion is BaseContinuationImpl) {
                // unrolling recursion via loop
                current = completion
                param = outcome
            } else {
                //注释3处，最终执行完毕
                completion.resumeWith(outcome)
                return
            }
        }
    }
}
```

注释1处，这里的invokeSuspend就是我们反编译出来的Function2对象的invokeSuspend方法。这时候具体的调用是：


```
println("Hello world! GlobalScope first after delay current Thread ${Thread.currentThread().name}")
//第二个挂起点
delay(200)
```

执行到`delay(200)`的时候，协程挂起了，直接return。这时候第2阶段执行完毕。

然后是delay结束后再次resume，最终还是调用`ContinuationImpl`的resumeWith方法。这个方法在BaseContinuationImpl类中就实现了。

```kotlin
public final override fun resumeWith(result: Result<Any?>) {
    // This loop unrolls recursion in current.resumeWith(param) to make saner and shorter stack traces on resume
    var current = this
    var param = result
    while (true) {
        // Invoke "resume" debug probe on every resumed continuation, so that a debugging library infrastructure
        // can precisely track what part of suspended callstack was already resumed
        probeCoroutineResumed(current)
        with(current) {
            //这个completion通过debug，是我们传入的StandaloneCoroutine对象
            val completion = completion!! // fail fast when trying to resume continuation without completion
            val outcome: Result<Any?> =
                try {
                    //注释1处，这里的invokeSuspend就是我们反编译出来的Function2对象的invokeSuspend方法。如果是协程挂起了立即返回
                    val outcome = invokeSuspend(param)
                    if (outcome === COROUTINE_SUSPENDED) return
                    //注释2处，有计算结果了
                    Result.success(outcome)
                } catch (exception: Throwable) {
                    Result.failure(exception)
                }
            releaseIntercepted() // this state machine instance is terminating
            if (completion is BaseContinuationImpl) {
                // unrolling recursion via loop
                current = completion
                param = outcome
            } else {
                //注释3处，最终执行完毕
                completion.resumeWith(outcome)
                return
            }
        }
    }
}
```

这里的completion就是开始传入的StandaloneCoroutine对象。StandaloneCoroutine的resumeWith方法在AbstractCoroutine类中已经实现了。


```kotlin
/**
 * Completes execution of this with coroutine with the specified result.
 */
public final override fun resumeWith(result: Result<T>) {
    //注释1处，完成当前任务
    val state = makeCompletingOnce(result.toState())
    //如果状态是等待子协程结束，直接返回。
    if (state === COMPLETING_WAITING_CHILDREN) return
    afterResume(state)
}
```

注释1处，完成当前任务，就完了，就这么突兀的结束了，哈哈。


总结一下执行过程，其实很简单：

1. 协程代码执行。
2. 遇到挂起点以后，阻塞当前线程。
3. 到了阻塞的时间后，线程继续向下执行。

感觉这三句话跟没说一样，但是原理就是这么简单。


到这里，一个最简单的协程的执行流程就分析完毕了。（看懂掌声--枭哥），哈哈。我还没看懂。。。

参考链接：

* [Kotlin Coroutines(协程) 完全解析（二），深入理解协程的挂起、恢复与调度](https://www.jianshu.com/p/2979732fb6fb)



