### 这个项目会逐渐用来替代 kt
### Learn Kotlin
1. 使用idea运行，如何给main函数传递参数
点击run->Edit configurations在program arguments一栏里面填入参数即可，多个参数使用空格分隔开

* Java的 `==` 操作符是比较引用值，但Kotlin 的 `==` 操作符是比较内容， `===` 才是比较引用值

* E:\AndroidStudioProjects\kotlin-base\src\main\kotlin\handbook 该目录下是掘金小册的相关内容

learncoroutine包下是协程相关知识。
### 协程

协程可以简化异步编程，可以顺序地表达程序，协程也提供了一种避免阻塞线程并用更廉价、更可控的操作替代线程阻塞的方法 – 协程挂起。

协程的本质：极大程度的复用线程，通过让线程满载运行，达到最大程度的利用CPU，进而提升应用性能。
