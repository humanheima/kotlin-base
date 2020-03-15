package learncoroutine

import kotlinx.coroutines.*

fun mainTest() = runBlocking<Unit> {
    launch {
        // 默认继承 parent coroutine 的 CoroutineDispatcher，指定运行在 main 线程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(100)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
}

private fun main1(args: Array<String>) {
    GlobalScope.launch {
        val token = requestToken()
        println(token)
        val post = createPost(token, Item("123"))
        processPost(post)
        delay(1000)
    }
}

suspend fun requestToken(): Token {
    return Token("token")
}

suspend fun createPost(token: Token, item: Item): Post {
    return Post("coroutines")
}

fun processPost(post: Post) {
    println("process post$post")
}

data class Token(var token: String)

data class Item(var id: String)

data class Post(var name: String)

