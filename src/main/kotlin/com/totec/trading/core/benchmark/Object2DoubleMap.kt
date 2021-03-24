/**
 * Created by ndrsh on 7/15/20
 */

package com.totec.trading.core.benchmark

import net.openhft.smoothie.SmoothieMap
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
open class SmoothieMapBench {
	
	@State(Scope.Benchmark)
	open class MapState{
		val maxIndex = 5_000_000
		val stringArray = Array(maxIndex) { Random.nextLong().toString() }
		val doubleArray = DoubleArray(maxIndex) { Random.nextDouble()}
	}
	
	@Benchmark
	fun smoothieAdd(state: MapState, hole: Blackhole): MapState {
		val smoothieMap = SmoothieMap<String, Double>()
		for (i in 0 until state.maxIndex) {
			smoothieMap[state.stringArray[i]] = state.doubleArray[i]
		}
		hole.consume(smoothieMap)
		return state
	}
}


// care, this fastutil is not imported because it is useless
/*@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 50, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 50, timeUnit = TimeUnit.MILLISECONDS)
open class DoubleMapBench {
	
	@State(Scope.Benchmark)
	open class MapState{
		val maxIndex = 50_000_000
		val stringArray = Array(maxIndex) { Random.nextLong().toString() }
		val doubleArray = DoubleArray(maxIndex) {i -> Random.nextDouble()}
	}
	
	@Benchmark
	fun doubleMapAdd(state: MapState, hole: Blackhole): MapState {
		val doubleMap = Object2DoubleOpenHashMap<String>()
		for (i in 0 until state.maxIndex) doubleMap[state.stringArray[i]] = state.doubleArray[i]
		doubleMap.clear()
		hole.consume(hole)
		return state
	}
}*/

fun main() {
	SmoothieMapBench::class.bench()
	// DoubleMapBench::class.bench()
}