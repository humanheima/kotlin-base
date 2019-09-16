package handbook.thirty_noe

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlin.coroutines.CoroutineContext

/**
 * Crete by dumingwei on 2019-09-09
 * Desc: 协程通道
 *
 */

/**
 * Select 表达式:Select 表达式能够同时等待多个 suspending function，然后选择第一个可用的结果。
 */
fun main(args: Array<String>) = runBlocking<Unit> {

    val tony = selectProduce1(coroutineContext)
    val monica = selectProduce2(coroutineContext)

    repeat(10) {
        selectProduces(tony, monica)
    }

    coroutineContext.cancelChildren()
}

fun main6(args: Array<String>) = runBlocking<Unit> {

    val tony = selectProduce1(coroutineContext)
    val monica = selectProduce2(coroutineContext)

    repeat(10) {
        selectProduces(tony, monica)
    }

    coroutineContext.cancelChildren()
}

fun selectProduce1(context: CoroutineContext) = GlobalScope.produce<String>(context) {
    while (true) {
        delay(400)
        send("Tony")
    }
}

fun selectProduce2(context: CoroutineContext) = GlobalScope.produce<String>(context) {
    while (true) {
        delay(600)
        send("monica")
    }
}

suspend fun selectProduces(channel1: ReceiveChannel<String>, channel2: ReceiveChannel<String>) {
    select<Unit> {
        channel1.onReceive {
            println("This is $it")
        }
        channel2.onReceive {
            println("This is $it")
        }
    }
}

/**
 * actor
 */
fun main5(args: Array<String>) = runBlocking<Unit> {

    val summer = actor<Int>(coroutineContext) {
        var sum = 0
        for (i in channel) {
            sum += i
            println("Sum = $sum")
        }

    }

    repeat(10) { i ->
        summer.send(i + 1)
    }

    summer.close()
}

/**
 * Channel缓冲
 */
fun main4(args: Array<String>) = runBlocking<Unit> {

    val channel = Channel<Int>(2)
    launch(coroutineContext) {
        repeat(6) {
            delay(50)
            println("Sending $it")
            channel.send(it)
        }
    }

    launch {
        delay(1000)
        repeat(6) {
            println("Receive ${channel.receive()}")
        }
    }

    delay(20000)

}

/**
 * Pipelines
 */
fun main3(args: Array<String>) = runBlocking {

    val numbers = produce1()
    val squares = produce2(numbers)
    val adds = produce3(squares)

    adds.consumeEach(::println)

    println("Receive Done!")
    // 消费完消息之后，关闭所有的produce
    adds.cancel()
    squares.cancel()
    numbers.cancel()

}

fun produce1() = GlobalScope.produce(Dispatchers.Default) {
    repeat(5) { i ->
        send(i)
    }
}

fun produce2(numbers: ReceiveChannel<Int>) = GlobalScope.produce(Dispatchers.Default) {
    for (x in numbers) {
        send(x * x)
    }
}

fun produce3(numbers: ReceiveChannel<Int>) = GlobalScope.produce(Dispatchers.Default) {
    for (x in numbers) {
        send(x + 1)
    }
}


/**
 * 关闭管道
 */
fun main2(args: Array<String>) = runBlocking {

    val channel = Channel<Int>()  //定义一个通道
    launch(Dispatchers.Default) {

        repeat(5) { i ->
            delay(200)
            channel.send((i + 1) * (i + 1))

            if (i == 2) {  // 发送3次后关闭

                channel.close()
            }
        }
    }

    launch(Dispatchers.Default) {
        repeat(5) {
            try {
                println(channel.receive())
            } catch (e: ClosedReceiveChannelException) {
                println("There is a ClosedReceiveChannelException.") // channel 异常则打印
            }
        }
    }

    delay(2000)

    println("Receive Done!")

}

fun main1(args: Array<String>) = runBlocking {

    val channel = Channel<Int>()

    launch(Dispatchers.Default) {
        repeat(5) { i ->
            delay(200)
            channel.send((i + 1) * (i + 1))
        }
    }

    launch(Dispatchers.Default) {
        repeat(5) {
            println(channel.receive())
        }
    }

    delay(2000)

    println("Receive Done!")

}