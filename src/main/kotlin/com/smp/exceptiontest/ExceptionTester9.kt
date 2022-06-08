package com.smp.exceptiontest

import kotlinx.coroutines.*

/**
 * First example. In the testingScope, there are 4 coroutines involved.
 * Coroutine:A - Grand parent (created by launch builder)
 * Coroutine:B - Parent (created by launch builder)
 * Coroutine:C-1 - Child (created by launch builder)
 * Coroutine:C-2 - Child (created by launch builder), throwing exception.
 */
object ExceptionTester9 {
    private val testingScope = CoroutineScope(CoroutineName("TestingScope"))

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        with(testingScope) {
            executeTest().join()
        }
    }

    private fun CoroutineScope.executeTest(): Job =
        launch(CoroutineName("Coroutine:A")) {

            launch(CoroutineName("Coroutine:B")) {

                supervisorScope {
                    val sumOf1to10 = async(CoroutineName("Coroutine:C-1")) {
                        slowRangeSum(coroutineName = "Coroutine:C-1", from = 1, to = 10)
                    }

                    val sumOf11To25 = async(CoroutineName("Coroutine:C-2")) {
                        slowRangeSum(coroutineName = "Coroutine:C-2", from = 11, to = 25)
                    }

                    runCatching {
                        println("Sum of 1 to 25 is ${sumOf1to10.await() + sumOf11To25.await()}")
                    }.onFailure { throwable ->
                        println("Failed to calculate sum of 1 to 25. $throwable")
                    }
                }

            }
        }

    private suspend fun slowRangeSum(coroutineName: String, from: Int, to: Int): Int {
        return (from..to).reduce { acc, num ->
            delay(200L)
            if (acc + num > 80) throw IllegalStateException("I don't understand the number over 80.")
            println("[$coroutineName] : $acc + $num = ${acc + num}")
            acc + num
        }
    }
}