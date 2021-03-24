/**
 * Created by ndrsh on 18.11.20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.instrument.book.Side

data class Liquidation(val orderID: String, val price: Double, val amount: Double, val side: Side, val symbol: String, val timestamp: Long) {
	
	override fun equals(other: Any?): Boolean {
		return orderID == other
	}
	
	override fun hashCode(): Int {
		return orderID.hashCode()
	}
}
