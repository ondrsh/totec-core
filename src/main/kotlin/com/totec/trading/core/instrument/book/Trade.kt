/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.instrument.book

import com.totec.trading.core.instrument.book.ops.BookOp
import com.totec.trading.core.utils.getInvalid
import com.totec.trading.core.utils.recycling.FastPool
import com.totec.trading.core.utils.recycling.Recyclable


// @Suppress("DataClassPrivateConstructor")
class Trade
/**
 * Generic trade class for all exchanges. Does not implement equals() or hashCode().
 *
 * @property price
 * @property amount Is always positive, even for sells
 * @property initiatingSide Either [Side.Buy] or [Side.Sell]
 * @property timestamp Epoch millis
 */
private constructor() : Priceable, Recyclable {
	
	override var price: Double = Double.getInvalid()
	var amount: Double = Double.getInvalid()
	var initiatingSide: Side = Side.Buy
	var timestamp: Long = Long.getInvalid()
	override var count: Int = 0
	
	override fun backIntoPoolPrivate() {
		Trade.backIntoPool(this)
	}
	
	companion object : FastPool<Trade>() {
		override val initialArraySize: Int = 2048
		override var arr: Array<Trade> = Array(initialArraySize) { createEmpty() }
		
		fun getNext(price: Double, amount: Double, initiatingSide: Side, timestamp: Long): Trade {
			val next = Trade.getNext()
			next.price = price
			next.amount = amount
			next.initiatingSide = initiatingSide
			next.timestamp = timestamp
			next.count++
			return next
		}
		
		override fun createEmpty() = Trade()
		
		override fun createArray(newSize: Int) = Array(newSize) { arrayInit(it) }
	}
}
