/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.pooled.bookentry


/*
class BookEntryArray4 private constructor() {
	private var arr = Array(4) { BookEntry.createDefault() }
	
	operator fun get(index: Int) = arr[index]
	
	operator fun set(index: Int, entry: BookEntry) {
		arr[index] = entry
	}
	
	companion object : FastPool<BookEntryArray4>() {
		override var arr = Array(512) { BookEntryArray4() }
		
		override fun createDefault() = BookEntryArray4()
		
		override fun createArray(newSize: Int) = Array(newSize) { createDefault() }
	}
}*/
