/**
 * Created by ndrsh on 5/30/20
 */

package com.totec.trading.core.pooled.trade

import com.totec.trading.core.instrument.book.Trade
import com.totec.trading.core.utils.recycling.FastPool


class TradeArray64 private constructor() {
	private var arr = Array(64) { Trade.createEmpty() }
	
	operator fun get(index: Int) = arr[index]
	
	operator fun set(index: Int, trade: Trade) {
		arr[index] = trade
	}
	
/*	companion object : FastPool<TradeArray64>() {
		override val initialArraySize: Int = 8
		override var arr = Array(initialArraySize) { TradeArray64() }
		
		override fun createEmpty() = TradeArray64()
		
		override fun createArray(newSize: Int) = Array(newSize) { createEmpty() }
	}*/
}
