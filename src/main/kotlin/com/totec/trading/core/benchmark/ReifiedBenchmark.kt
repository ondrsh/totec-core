/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.benchmark

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
open class ReifiedBenchmark {
	
	@State(Scope.Thread)
	open class MyState {
		val arr = Array(10) { 2.0 }
	}
	
	@Benchmark
	fun withReified(state: MyState): Double {
		return reifyGet(state.arr)
	}
	
	@Benchmark
	fun withoutReified(state: MyState): Double {
		return normalGet(state.arr)
	}
	
	private inline fun <reified T> reifyGet(element: Array<T>): T {
		val size = element.size
		return element[size/2]
	}
	
	private fun normalGet(element: Array<Double>) = element[element.size/2]
}

fun main() {
	ReifiedBenchmark::class.bench()
}