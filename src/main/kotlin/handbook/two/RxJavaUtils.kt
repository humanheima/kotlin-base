package handbook.two

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

/**
 * Created by dmw on 2019/1/3.
 * Desc:
 */
object RxJavaUtils {


    /**
     * 1. 使用默认参数
     */
    @JvmOverloads //如果希望也向 Java 调用者暴露多个重载，可以使用 @JvmOverloads 注解
    @JvmStatic //表示该方法为静态方法
    fun <T> preventDuplicateClicksTransformer(
        windowDuration: Long = 1000,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS
    ): ObservableTransformer<T, T> {


        //return ObservableTransformer<T, T> { upstream -> upstream.throttleFirst(windowDuration, timeUnit) }

        return ObservableTransformer { upstream -> upstream.throttleFirst(windowDuration, timeUnit) }

    }

    /**
     * Java 写法
     */
    @JvmOverloads //如果希望也向 Java 调用者暴露多个重载，可以使用 @JvmOverloads 注解
    @JvmStatic //表示该方法为静态方法
    fun <T> preventDuplicateClicksTransformerJava(
        windowDuration: Long = 1000,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS
    ): ObservableTransformer<T, T> {

        val transform = object : ObservableTransformer<T, T> {
            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                return upstream.throttleFirst(windowDuration, timeUnit)
            }
        }

        return transform

    }


}