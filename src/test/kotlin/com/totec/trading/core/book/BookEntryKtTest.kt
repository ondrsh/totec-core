package com.totec.trading.core.book

import com.totec.trading.core.instrument.book.BookEntry
import com.totec.trading.core.instrument.book.addAmount
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.shouldBeExactly

class BookEntryKtTest : StringSpec() {
	
	init {
		"apply change with operator" {
			val entry = BookEntry.getNext(200.0, 50.0, System.currentTimeMillis())
			val changeMinus = BookEntry.getNext(200.0, -4.5, System.currentTimeMillis())
			entry.addAmount(changeMinus)
			entry.amount shouldBeExactly 45.5
			
			val changePlus = BookEntry.getNext(200.0, 5.3, System.currentTimeMillis())
			entry.addAmount(changePlus)
			entry.amount shouldBeExactly 50.8
			
			entry.decCount()
			changeMinus.decCount()
			changePlus.decCount()
		}
	}
}