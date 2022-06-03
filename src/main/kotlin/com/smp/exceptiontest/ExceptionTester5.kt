package com.smp.exceptiontest

import kotlinx.coroutines.*

/**
 * First example. In the testingScope, there are 4 coroutines involved.
 * Coroutine:A - Grand parent (created by async builder)
 * Coroutine:B - Parent (created by launch builder)
 * Coroutine:C-1 - Child (created by launch builder)
 * Coroutine:C-2 - Child (created by launch builder), throwing exception.
 *
 * Call await() function for the Deferred from async builder.
 */
object ExceptionTester5 {
    private val testingScope = CoroutineScope(CoroutineName("TestingScope"))

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        with(testingScope) {
            executeTest().await()
        }
    }

    private fun CoroutineScope.executeTest(): Deferred<Unit> =
        async(CoroutineName("Coroutine:A")) {

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