package com.smp.exceptiontest

import kotlinx.coroutines.*
import java.util.concurrent.Executors

/**
 * First example. In the testingScope, there are 4 coroutines involved.
 * Coroutine:A - Grand parent (created by launch builder)
 * Coroutine:B - Parent (created by launch builder)
 * Coroutine:C-1 - Child (created by launch builder)
 * Coroutine:C-2 - Child (created by launch builder), throwing exception.
 */
object ExceptionTester3 {
    private val singleThreadedDispatcher = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable).apply {
            // Set uncaught exception handler for the thread
            // setUncaughtExceptionHandler { _, exception -> println("UncaughtExceptionHandler : $exception") }
        }
    }.asCoroutineDispatcher()
    private val testingScope = CoroutineScope(singleThreadedDispatcher)

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        // Set uncaught exception handler for all threads
        // Thread.setDefaultUncaughtExceptionHandler { thread, exception -> println("DefaultUncaughtExceptionHandler : $exception") }
        singleThreadedDispatcher.use {
            with(testingScope) {
                executeTest().join()
            }
        }
    }

    private fun CoroutineScope.executeTest(): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler : $exception")
        }
        // Set coroutine exception handler
       return launch(CoroutineName("Coroutine:A") /* + exceptionHandler */) {

            launch(CoroutineName("Coroutine:B")) {

                launch(CoroutineName("Coroutine:C-1")) {
                    repeat(3) {
                        delay(1000)
                        log("Processing : ${it + 1} / 3")
                    }
                }

                launch(CoroutineName("Coroutine:C-2")) {
                    repeat(6) {
                        delay(500)
                        log("Processing : ${it + 1} / 5")
                        if (it > 0) throw RuntimeException("Something wrong!")
                    }
                }

            }
        }
    }
}