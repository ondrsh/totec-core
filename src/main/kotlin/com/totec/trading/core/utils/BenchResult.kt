/**
 * Created by ndrsh on 5/31/20
 */

package com.totec.trading.core.utils

/**
 * Helper class for custom benchmarks. Units are in **milliseconds** so divide if you need
 * micros or nanos
 *
 * @property perTest
 * @property perIter
 */
data class BenchResult(val perTest: Double, val perIter: Double)
