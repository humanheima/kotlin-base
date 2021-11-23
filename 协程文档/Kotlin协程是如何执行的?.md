最简单的一段协程代码
```kotlin
fun main() {
    GlobalScope.launch {
        println("Hello world!")
    }
}
```

将Kotlin字节码反编译成Java代码

```java
public final class BasicKt {
   public static final void main() {
      BuildersKt.launch$default((CoroutineScope)GlobalScope.INSTANCE, (CoroutineContext)null, (CoroutineStart)null, (Function2)(new Function2((Continuation)null) {
         private CoroutineScope p$;
         int label;

         @Nullable
         public final Object invokeSuspend(@NotNull Object $result) {
            Object var5 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
            case 0:
               ResultKt.throwOnFailure($result);
               CoroutineScope $this$launch = this.p$;
               String var3 = "Hello world!";
               boolean var4 = false;
               System.out.println(var3);
               return Unit.INSTANCE;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
         }

         @NotNull
         public final Continuation create(@Nullable Object value, @NotNull Continuation completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            Function2 var3 = new <anonymous constructor>(completion);
            var3.p$ = (CoroutineScope)value;
            return var3;
         }

         public final Object invoke(Object var1, Object var2) {
            return ((<undefinedtype>)this.create(var1, (Continuation)var2)).invokeSuspend(Unit.INSTANCE);
         }
      }), 3, (Object)null);
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }
}
```

CoroutineScope.launch方法

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
注释1处，默认使用StandaloneCoroutine。构造函数中传递了`parentContext`和`active`两个参数。

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
注释1处，初始化父Job。里面会将协程与父协程关联，这样父协程才会等待所有子协程结束以后自己才结束。这不是本文的重点，可以暂时忽略。

注释2处，这里注意一下，这里的`start(block, receiver, this)`是调用的CoroutineStart中的`invoke`方法并不是AbstractCoroutine类中定义的 start 方法。

CoroutineStart是一个枚举类，枚举值有以下几个。


* DEFAULT： -- 根据上下文立即调度执行协程

* LAZY：-- 启动一个惰性协程

* ATOMIC：-- 根据上下文自动调度执行协程，和DEFAULT类似，但是这种类型的协程在开始执行之前不能取消

* UNDISPATCHED： -- 在当前线程立即执行协程知道第一个挂起点。协程从第一个挂起点恢复以后是在哪个线程是不确定的。


CoroutineStart类的 invoke 方法

传入的completion参数是一个StandaloneCoroutine对象。

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
public fun <T> (suspend () -> T).startCoroutineCancellable(completion: Continuation<T>): Unit = 
        runSafely(completion) {
    createCoroutineUnintercepted(completion).intercepted().resumeCancellableWith(Result.success(Unit))
}
```

首先调用的是`runSafely`方法来执行指定的代码块并且当协程出现异常的时候完成，就是调用StandaloneCoroutine的resumeWith方法。

```kotlin
private inline fun runSafely(completion: Continuation<*>, block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        //出现异常的时候完成
        completion.resumeWith(Result.failure(e))
    }
}
```

接下来我们看一看传递给runSafely的代码块。首先是`createCoroutineUnintercepted(completion)`，这个方法是在`IntrinsicsJvm.kt`中实现的。

```kotlin
/**
 * 返回不可不可拦截的continuation.
 */
@SinceKotlin("1.3")
public actual fun <T> (suspend () -> T).createCoroutineUnintercepted(
    completion: Continuation<T>
): Continuation<Unit> {
    val probeCompletion = probeCoroutineCreated(completion)
    return if (this is BaseContinuationImpl)
         //注释1处
        create(probeCompletion)
    else
        createCoroutineFromSuspendFunction(probeCompletion) {
            (this as Function1<Continuation<T>, Any?>).invoke(it)
        }
}
```

注释1处，我的理解是：这里的create方法最终使用编译器实现的，就是我们上面反编译出来的代码。

```kotlin
@NotNull
public final Continuation create(@Nullable Object value, Continuation completion) {
    Intrinsics.checkParameterIsNotNull(completion, "completion");
    Function2 var3 = new <anonymous constructor>(completion);
    var3.p$ = (CoroutineScope)value;
    return var3;
}
```

那么注释1处返回的对象到底是个什么东西呢？在debug的时候使用IDEA的Evaluate功能来评估注释1处的表达式结果如下图所示：

![create_result.png](https://upload-images.jianshu.io/upload_images/3611193-02a757ad67236c6f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`Continuation at learncoroutine.docs.BasicKt$main$job$1.invokeSuspend(Basic.kt)`，结果是我们反编译出来的代码中invokeSuspend方法返回的对象。

再把上面的代码贴一下。

```
createCoroutineUnintercepted(completion).intercepted().resumeCancellableWith(Result.success(Unit))


```
`createCoroutineUnintercepted(completion)`这一步走完了，下面看`intercepted()`这一步。

这个方法是定义在IntrinsicsJvm.kt文件中的Continuation的扩展函数。

```kotlin
@SinceKotlin("1.3")
public actual fun <T> Continuation<T>.intercepted(): Continuation<T> =
    (this as? ContinuationImpl)?.intercepted() ?: this
```

通过debug发现`(this as? ContinuationImpl)`是条件是满足的，也就是说我们反编译出来的代码中invokeSuspend方法返回的对象可以看做是一个ContinuationImpl。那么接下来我们看一下ContinuationImpl的intercepted方法。

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
默认的调度器是Dispatchers.Default，默认是一个DefaultScheduler对象。注释1处，调度器开始调度。我们注意一下，dispatch方法的第二个参数是一个Runnable对象。我们可以猜测，当Runnable被调度的时候肯定会调用Runnable的run方法。

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

```kotlin
public inline fun <T> Continuation<T>.resume(value: T): Unit =
    resumeWith(Result.success(value))
```
然后调用ContinuationImpl的resumeWith方法。这个方法在BaseContinuationImpl类中就实现了。

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
            val completion = completion!! // fail fast when trying to resume continuation without completion
            val outcome: Result<Any?> =
                try {
                    //注释1处，如果是协程挂起了立即返回
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
注释3处的completion就是开始的StandaloneCoroutine对象。

StandaloneCoroutine继承了AbstractCoroutine

AbstractCoroutine
```
public final override fun resumeWith(result: Result<T>) {
    val state = makeCompletingOnce(result.toState())
     //如果状态是等待子协程完成就直接返回
    if (state === COMPLETING_WAITING_CHILDREN) return
    afterResume(state)
}

protected open fun afterResume(state: Any?): Unit = afterCompletion(state)
```
afterResume方法是 JobSupport 中定义的方法。AbstractCoroutine继承了JobSupport类。

JobSupport的afterCompletion方法，什么都没有做。
```kotlin
protected open fun afterCompletion(state: Any?) {}
```

到这里，一个最简单的协程的执行流程就分析完毕了。（看懂掌声--枭哥），哈哈。

参考链接：

* [Kotlin Coroutines(协程) 完全解析（二），深入理解协程的挂起、恢复与调度](https://www.jianshu.com/p/2979732fb6fb)



