/**
 * Created by ndrsh on 02.12.20
 */

package com.totec.trading.core.engines

interface LagEngine: Engine {
	
	fun addTradeLag(lag: Long, timestamp: Long)
	
	fun addBookLag(lag: Long, timestamp: Long)
	
	fun addInstrumentLag(lag: Long, timestamp: Long)
}
