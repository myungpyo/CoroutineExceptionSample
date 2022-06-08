package com.smp.exceptiontest

import kotlinx.coroutines.*

/**
 * First example. In the testingScope, there are 4 coroutines involved.
 * Coroutine:A - Grand parent (created by launch builder)
 * Coroutine:B-1 - Parent (created by launch builder)
 * Coroutine:B-2 - Parent (created by launch builder with SupervisorJob)
 * Coroutine:C-1 - Child (created by launch builder)
 * Coroutine:C-2 - Child (created by launch builder), throwing exception.
 */
object ExceptionTester7E {
    private val testingScope = CoroutineScope(CoroutineName("TestingScope"))

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        with(testingScope) {
            executeTest().join()
        }
    }

    private fun CoroutineScope.executeTest(): Job =
        launch(CoroutineName("Coroutine:A")) {

            launch(CoroutineName("Coroutine:B-1")) {
                repeat(5) {
                    delay(1000)
                    log("Processing : ${it + 1} / 3")
                }

            }

            supervisorScope {
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