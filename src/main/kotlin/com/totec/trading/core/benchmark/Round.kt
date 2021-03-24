/**
 * Created by ndrsh on 5/13/20
 */

package com.totec.trading.core.benchmark

import org.openjdk.jmh.annotations.*
import com.totec.trading.core.utils.roundPrice
import com.totec.trading.core.utils.roundPriceSafe
import com.totec.trading.core.utils.roundSafe
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.math.roundToLong

const val n = 1_000

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
open class Round {
	
	@State(Scope.Thread)
	open class MyState {
		val l1 = 129418000005000L
		val l2 = 388215000002000L
		val d1 = 1294.18000005
		val d2 = 3882.15000002
		val f1 = 1294.18000005f
		val f2 = 3882.15000002f
	}
	
	@Benchmark
	fun doubleRoundSafe(state: MyState) = (state.d1 * 100_000_000).roundSafe()
	
	@Benchmark
	fun doubleRound(state: MyState) = (state.d1 * 100_000_000).roundToLong()
	
	@Benchmark
	fun roundPrice(state: MyState) = state.d1.roundPrice()
	
	@Benchmark
	fun roundPriceTry(state: MyState) = state.d1.roundPriceSafe()
	
	/*@Benchmark
	fun longDivision(state: MyState) = state.l1 / 6
	
	@Benchmark
	fun doubleDivision(state: MyState) = state.d1 / 6
	
	@Benchmark
	fun floatDivision(state: MyState) = state.f1 / 6
	
	@Benchmark
	fun floatRound(state: MyState) = state.f1.roundInline()
	
	@Benchmark
	fun floatSubtract(state: MyState) = state.f2 - state.f1
	
	@Benchmark
	fun floatSubtractWithRound(state: MyState) = (state.f2 - state.f1).roundInline()
	
	@Benchmark
	fun convertToLong(state: MyState) = state.d1.priceToLong()
	
	@Benchmark
	fun convertToLongInline(state: MyState) = state.d1.priceToLongInline()
	
	@Benchmark
	fun roundDoubleInline(state: MyState) = state.d1.roundInline()
	
	@Benchmark
	fun substractDoubleWithRound(state: MyState) = (state.d2 - state.d1).roundInline()
	
	@Benchmark
	fun substractLong(state: MyState) = (state.l2 - state.l1)
	
	@Benchmark
	fun substractLongFunction(state: MyState) = subtractSecondFromFirstLong(state.l2, state.l1)
	
	@Benchmark
	fun substractLongExtension(state: MyState) = state.l2.substractLong(state.l1)*/
	
	private fun subtractSecondFromFirstLong(first: Long, second: Long) = first - second
	
	private fun Long.substractLong(other: Long) = this - other
	
}

fun Double.round(places: Int): Double {
	val factor: Double = Math.pow(10.0, places * 1.0)
	return (this * factor).roundToInt() / factor
}

fun Double.roundInline(): Double {
	return (this * 10_000_000_000.0).roundToInt() / 10_000_000_000.0
}

fun Float.roundInline(): Float {
	return (this * 10_000_000_000.0f).roundToLong() / 10_000_000_000.0f
}

fun main() {
	Round::class.bench()
	/*
		val options = OptionsBuilder()
			.include(Round::class.java.simpleName)
			.output("benchmark_sequence.log")
			.build()
		Runner(options).run()
	*/
}