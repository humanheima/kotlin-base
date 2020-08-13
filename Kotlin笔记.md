## 协程的启动

```kotin
public enum class CoroutineStart {

    DEFAULT,

    LAZY,

    @ExperimentalCoroutinesApi
    ATOMIC,

   

    /**
     * 立即在当前线程执行协程直到它的第一个挂起点。但是，当这个协程从挂起恢复的时候，它将根据它的上下文中指定的[CoroutineDispatcher]被调度。
     *
     * This is similar to [ATOMIC] in the sense that coroutine starts executing even if it was already cancelled,
     * but the difference is that it starts executing in the same thread.
     *
     * Cancellability of coroutine at suspension points depends on the particular implementation details of
     * suspending functions as in [DEFAULT].
     *
     */
    @ExperimentalCoroutinesApi
    UNDISPATCHED;
}

```

### CoroutineContext

### 拦截器

### 调度器


