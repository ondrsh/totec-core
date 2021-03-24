/**
 * Created by ndrsh on 6/2/20
 */

package com.totec.trading.core.instrument.book.ops

import com.totec.trading.core.utils.getInvalid
import com.totec.trading.core.utils.recycling.FastPool
import com.totec.trading.core.utils.recycling.Recyclable

/**
 * Does not implement equals() or hashCode().
 */
open class BookOp private constructor() : Recyclable {
	
	var type: OpType = OpType.INSERT
	var price: Double = Double.getInvalid()
	var amount: Double = Double.getInvalid()
	var survived: Long = Long.getInvalid()
	var timestamp: Long = Long.getInvalid()
	var distanceToTop: Double = Double.getInvalid()
	override var count: Int = 0
	
	override fun backIntoPoolPrivate() {
		BookOp.backIntoPool(this)
	}
	
	companion object : FastPool<BookOp>() {
		override val initialArraySize: Int = 2048
		override var arr: Array<BookOp> = Array(initialArraySize) { createEmpty() }
		
		fun getInsert(price: Double, amount: Double, timestamp: Long): BookOp {
			val next = getNext()
			next.type = OpType.INSERT
			next.price = price
			next.amount = amount
			next.timestamp = timestamp
			next.count++
			return next
		}
		
		fun getChange(price: Double, changeAmount: Double, timestamp: Long): BookOp {
			val next = getNext()
			next.type = OpType.CHANGE
			next.price = price
			next.amount = changeAmount
			next.timestamp = timestamp
			next.count++
			return next
		}
		
		fun getDelete(price: Double, timestamp: Long): BookOp {
			val next = getNext()
			next.type = OpType.DELETE
			next.price = price
			next.timestamp = timestamp
			next.count++
			return next
		}
		
		override fun createEmpty() = BookOp()
		
		override fun createArray(newSize: Int) = Array(newSize) { arrayInit(it) }
	}
}

