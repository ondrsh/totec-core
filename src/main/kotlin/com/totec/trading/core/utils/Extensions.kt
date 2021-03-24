/**
 * Created by ndrsh on 5/15/20
 */

package com.totec.trading.core.utils


/**
 * This is needed, otherwise we get floating point errors when adding (because 0.1 + 0.2 != 0.3).
 */
fun Double.roundPrice(): Double {
    return (this * 10_000_000_000).roundSafe() / 10_000_000_000.0
}

/**
 * Safe version of [roundAmount] which can fail.
 */
fun Double.roundPriceSafe(): Double {
    return try {
        (this * 10_000_000_000).roundSafe() / 10_000_000_000.0
    } catch (ex: Exception) {
        ex.printStackTrace()
        this
    }
}

/**
 * This is needed, otherwise we get floating point errors when adding (because 0.1 + 0.2 != 0.3).
 */
fun Double.roundAmount(): Double {
    return (this * 1_000_000_000).roundSafe()/1_000_000_000.0
}

/**
 * Safe version of [roundAmount] which can fail.
 */
fun Double.roundAmountSafe(): Double {
    return try {
        roundAmount()
    } catch (ex: Exception) {
        ex.printStackTrace()
        this
    }
}

fun Double.roundSafe(): Long = when {
    isNaN() -> throw IllegalArgumentException("Cannot round NaN value.")
    this > Long.MAX_VALUE -> throw IllegalStateException("Tried to round value $this to Long, but it overflows")
    this < Long.MIN_VALUE -> throw IllegalStateException("Tried to round value $this to Long, but it overflows")
    else -> Math.round(this)
}

fun <T> MutableList<T>.removeAllButLast() {
    val last = last()
    clear()
    add(last)
}

fun Double.isValid() = !isNaN()
fun Double.isInvalid() = isNaN()
fun Double.Companion.getInvalid() = NaN
fun Long.isValid() = this != Long.getInvalid()
fun Long.isInvalid() = this == Long.getInvalid()
fun Long.Companion.getInvalid() = MIN_VALUE

fun linspace(start: Int, stop: Int, num: Int) = (start..stop step (stop - start) / (num - 1)).toList()
