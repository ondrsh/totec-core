package com.totec.trading.core.utils.recycling

import com.totec.trading.core.instrument.book.BookEntry
import com.totec.trading.core.instrument.book.Side
import com.totec.trading.core.instrument.book.Trade
import com.totec.trading.core.instrument.book.ops.BookOp
import com.totec.trading.core.utils.readInstanceProperty
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance

class FastPoolTest : StringSpec(
	{
		
		"BookEntry FastPool" {
			BookEntry.resetPool()
			
			val iters = BookEntry.initialArraySize - 1
			repeat(iters) {
				BookEntry.getNext()
			}
			
			companionIndex(BookEntry::class) shouldBeExactly iters
			companionSize(BookEntry::class) shouldBeExactly BookEntry.initialArraySize
			val entry = BookEntry.getNext(9900.0, 10.0, System.currentTimeMillis())
			BookEntry.arr[BookEntry.arr.size / 2] shouldBe BookEntry.arr.last()
			BookEntry.arr[BookEntry.arr.size / 2].hashCode() shouldBeExactly  BookEntry.arr.last().hashCode()
			BookEntry.arr[BookEntry.arr.size / 2 - 1] shouldBeSameInstanceAs entry
			companionIndex(BookEntry::class) shouldBeExactly iters + 1
			companionSize(BookEntry::class) shouldBeExactly BookEntry.initialArraySize * 2
			entry.decCount()
			companionIndex(BookEntry::class) shouldBeExactly iters
			val newEntry = BookEntry.getNext(8000.0, 100_000.0, System.currentTimeMillis())
			companionIndex(BookEntry::class) shouldBeExactly iters + 1
			newEntry shouldBeSameInstanceAs entry
			companionSize(BookEntry::class) shouldBeExactly BookEntry.initialArraySize * 2
		}
		
		"Trade FastPool" {
			Trade.resetPool()
			
			val iters = Trade.initialArraySize - 1
			repeat(iters) {
				Trade.getNext()
			}
			
			companionIndex(Trade::class) shouldBeExactly iters
			companionSize(Trade::class) shouldBeExactly Trade.initialArraySize
			val entry = Trade.getNext(9900.0, 10.0, Side.Buy, System.currentTimeMillis())
			Trade.arr[Trade.arr.size / 2 - 1] shouldBeSameInstanceAs entry
			companionIndex(Trade::class) shouldBeExactly iters + 1
			companionSize(Trade::class) shouldBeExactly Trade.initialArraySize * 2
			entry.decCount()
			companionIndex(Trade::class) shouldBeExactly iters
			val newEntry = Trade.getNext(8000.0, 100_000.0, Side.Buy, System.currentTimeMillis())
			companionIndex(Trade::class) shouldBeExactly iters + 1
			newEntry shouldBeSameInstanceAs entry
			companionSize(Trade::class) shouldBeExactly Trade.initialArraySize * 2
		}
		
		"BookOp FastPool" {
			BookOp.resetPool()
			
			val iters = BookOp.initialArraySize - 1
			repeat(iters) {
				BookOp.getNext()
			}
			
			companionIndex(BookOp::class) shouldBeExactly iters
			companionSize(BookOp::class) shouldBeExactly BookOp.initialArraySize
			val entry = BookOp.getInsert(9900.0, 10.0, System.currentTimeMillis())
			BookOp.arr[BookOp.arr.size / 2 - 1] shouldBeSameInstanceAs entry
			companionIndex(BookOp::class) shouldBeExactly iters + 1
			companionSize(BookOp::class) shouldBeExactly BookOp.initialArraySize * 2
			entry.decCount()
			companionIndex(BookOp::class) shouldBeExactly iters
			val newEntry = BookOp.getInsert(8000.0, 100_000.0, System.currentTimeMillis())
			companionIndex(BookOp::class) shouldBeExactly iters + 1
			newEntry shouldBeSameInstanceAs entry
			companionSize(BookOp::class) shouldBeExactly BookOp.initialArraySize * 2
		}
	})

fun companionIndex(c: KClass<*>): Int {
	return readInstanceProperty(c.companionObjectInstance!!, "index")
}
fun companionSize(c: KClass<*>) = (c.companionObjectInstance!! as FastPool<*>).arr.size
