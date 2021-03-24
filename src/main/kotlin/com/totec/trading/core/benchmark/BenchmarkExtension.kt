/**
 * Created by ndrsh on 5/14/20
 */

package com.totec.trading.core.benchmark

import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import com.totec.trading.core.utils.io.createDir
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KType


/**
 * Makes it easier to execute JMH benchmarks.
 * @sample main() = JsonBenchmark::class.bench()
 * @author ndrsh
 */
fun KClass<*>.bench() {
	val benchmarkFolder = "benchmarks"
	benchmarkFolder.createDir()
	val name = this.java.simpleName
	val path = benchmarkFolder + File.separator + name
	path.createDir()
	val options = OptionsBuilder()
		.include(name)
		.output(path + File.separator + "benchmark.log")
		.build()
	Runner(options).run()
}

