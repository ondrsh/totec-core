/**
 * Created by ndrsh on 02.12.20
 */

package com.totec.trading.core.engines

import com.totec.trading.core.instrument.book.Trade

/**
 * Handle reference count of [Trade] objects. This is mandatory, failing to do so will crash the JVM.
 */
interface TradeEngine : Engine {
	fun addTrade(trade: Trade)
}
