/**
 * Created by ndrsh on 7/7/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.utils.roundAmountSafe
import com.totec.trading.core.utils.roundPriceSafe
import kotlin.math.min

/**
 * Class that represents things like [InstrumentInfo.lotSize] or [InstrumentInfo.tickSize]
 */
sealed class Size {
	var value = 0.0
	var isInitialized = false
	
	abstract fun calculate(book: Book)
	
	fun initializeWith(v: Double) {
		value = v
		isInitialized = true
	}
	
	fun initializeWith(other: Size) {
		if (other.isInitialized) {
			initializeWith(other.value)
		}
	}
	
	class Lot : Size() {
		@ExperimentalStdlibApi
		override fun calculate(book: Book) {
			val asksMinimum = book.asks.set
				.map { it.amount }
				.sorted()
				.scan(0.0) { amount, nextAmount -> (nextAmount - amount).roundAmountSafe() }
				.filter { it != 0.0 }
				.minOrNull()!!
			
			val bidsMinimum = book.bids.set
				.map { it.amount }
				.sorted()
				.scan(0.0) { amount, nextAmount -> (nextAmount - amount).roundAmountSafe() }
				.filter { it != 0.0 }
				.minOrNull()!!
			
			val min = min(asksMinimum, bidsMinimum)
			value = min
			isInitialized = true
		}
	}
	
	class Tick : Size() {
		@ExperimentalStdlibApi
		override fun calculate(book: Book) {
			val asksMinimum = book.asks.set
				.map { it.price }
				.sorted()
				.scan(0.0) { price, nextPrice -> (nextPrice - price).roundPriceSafe() }
				.filter { it != 0.0 }
				.minOrNull()!!
			
			val bidsMinimum = book.bids.set
				.map { it.amount }
				.sorted()
				.scan(0.0) { price, nextPrice -> (nextPrice - price).roundPriceSafe() }
				.filter { it != 0.0 }
				.minOrNull()!!
			
			val min = min(asksMinimum, bidsMinimum)
			value = min
			isInitialized = true
		}
	}
}
