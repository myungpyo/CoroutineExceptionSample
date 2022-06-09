package com.smp.exceptiontest

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class SimpleCoroutineExceptionHandler: CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler.Key

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("SimpleCoroutineExceptionHandler : $exception")
    }
}