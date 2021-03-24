package com.totec.trading.core.utils


/**
 * Simple helper function that can be used instead of JMH for quick
 * prototyping. It's obviously **less accurate** if you use it without your brain.
 * All units are in **milliseconds** so divide if you need micros or nanos.
 *
 * @param ITERATIONS How often to invoke the function per run
 * @param TEST_COUNT Number of rest runs
 * @param WARM_COUNT Number of warming runs
 * @param NAME Name of the Benchmark
 * @param block Function to invoke
 * @return [BenchResult]
 */
suspend fun simpleMeasureTest(
	ITERATIONS: Int = 1000,
	TEST_COUNT: Int = 10,
	WARM_COUNT: Int = 2,
	NAME: String = "Default Test",
	block: suspend () -> Unit
): BenchResult {
	
		val printPrefix = "[TimeTest]"
		val results = ArrayList<Double>()
		var totalTime = 0.0
		var t = 0
		
		println("$printPrefix -> go")
		
		while (++t <= TEST_COUNT + WARM_COUNT) {
			val startTime = System.nanoTime()
			
			var i = 0
			while (i++ < ITERATIONS)
				block()
			
			if (t <= WARM_COUNT) {
				println("$printPrefix Warming $t of $WARM_COUNT")
				continue
			}
			
			val time = (System.nanoTime() - startTime) / 1_000_000.0
			println(printPrefix + " " + time.toString() + "ms")
			
			results.add(time)
			totalTime += time
		}
		
		
		results.sort()
		
		val average = totalTime / TEST_COUNT
		val median = results[results.size / 2]
		
		println("$printPrefix $NAME -> average=${average}ms / median=${median}ms")
		//println("$PRINT_REFIX $NAME -> median operation time=${median/ITERATIONS}ms")
		println("$printPrefix $NAME -> average operation time=${average / ITERATIONS}ms")
		return BenchResult(perTest = average, perIter = average / ITERATIONS)
}
