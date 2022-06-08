package com.smp.exceptiontest

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.log(message: String) {
    println("${coroutineContext[CoroutineName]} : $message")
}

fun CoroutineScope.logWithThreadTag(message: String) {
    println("${Thread.currentThread().name}} : $message")
}