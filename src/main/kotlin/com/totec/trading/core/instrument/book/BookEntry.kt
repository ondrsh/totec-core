/**
 * Created by ndrsh on 5/13/20
 */

package com.totec.trading.core.instrument.book

import com.totec.trading.core.utils.getInvalid
import com.totec.trading.core.utils.isInvalid
import com.totec.trading.core.utils.recycling.FastPool
import com.totec.trading.core.utils.recycling.Recyclable
import com.totec.trading.core.utils.roundAmountSafe

/**
 * Generic entry class for all exchanges. Gets put into [Book].
 */

class BookEntry private constructor() : Recyclable {
	
	var price: Double = Double.getInvalid()
	var amount: Double = Double.getInvalid()
	var timestamp: Long = Long.getInvalid()
	override var count: Int = 0

	override fun equals(other: Any?): Boolean {
		return when {
			other !is BookEntry     -> false
			other.price.isInvalid() -> price.isInvalid()
			else                    -> other.price == price
		}
	}
	
	override fun hashCode() = price.hashCode()
	
	override fun toString(): String {
		return "[Entry: price=$price, amount=$amount, timestamp=$timestamp, count=$count]"
	}
	
	override fun backIntoPoolPrivate() {
		BookEntry.backIntoPool(this)
	}
	
	companion object : FastPool<BookEntry>() {
		override val initialArraySize: Int = 4096
		override var arr: Array<BookEntry> = Array(initialArraySize) { createEmpty() }
		
		fun getNext(price: Double, amount: Double, timestamp: Long): BookEntry {
			val next = getNext()
			next.price = price
			next.amount = amount
			next.timestamp = timestamp
			next.count++
			return next
		}
		
		override fun createEmpty() = BookEntry()
		
		override fun createArray(newSize: Int) = Array(newSize) { arrayInit(it) }
	}
}

fun BookEntry.addAmount(other: BookEntry) {
	this.amount = (this.amount + other.amount).roundAmountSafe()
}
