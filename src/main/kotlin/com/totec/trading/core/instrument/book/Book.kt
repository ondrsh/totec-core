/**
 * Created by ndrsh on 5/30/20
 */

package com.totec.trading.core.instrument.book

import com.totec.trading.core.instrument.book.bookside.Asks
import com.totec.trading.core.instrument.book.bookside.Bids
import kotlin.math.max

interface Book {
	val bids: Bids
	val asks: Asks
	val timestamp: Long
		get() = max(bids.lastUpdated, asks.lastUpdated)
	
	operator fun get(side: Side) = if (side == Side.Buy) bids else asks
	
	fun isInitialized() = timestamp != Long.MIN_VALUE
	
	fun lastUpdateTooLongAgo(newBook: Book) = (newBook.timestamp - timestamp) > 5_000_000 / ((newBook.bids.set.size + newBook.asks.set.size) / 2)
}
