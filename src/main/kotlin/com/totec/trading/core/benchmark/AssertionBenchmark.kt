/**
 * Created by ndrsh on 5/15/20
 */

package com.totec.trading.core.benchmark

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
open class AssertionBenchmark {
	
	@State(Scope.Benchmark)
	open class MyState {
		val d1 = 1.23123
		val d2 = 2.11515
		val d_limit = 3.151
	}
	
	@Benchmark
	fun withoutAssert(state: MyState): Double {
		val d3 = state.d1 + state.d2
		return d3
	}
	
	@Benchmark
	fun withAssert(state: MyState): Double {
		val d3 = state.d1 + state.d2
		assert(d3 > state.d_limit)
		assert(state.d1 < state.d2)
		assert(state.d1 < state.d_limit)
		return d3
	}
}

fun main() {
	AssertionBenchmark::class.bench()
}