package crack


interface Callback<T> {

    fun onSuccess(value: T)

    fun onError(t: Throwable)
}