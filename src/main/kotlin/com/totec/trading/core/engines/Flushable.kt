/**
 * Created by ndrsh on 02.12.20
 */

package com.totec.trading.core.engines

interface Flushable {
	fun flush(timestamp: Long)
}
