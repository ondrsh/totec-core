/**
 * Created by ndrsh on 5/30/20
 */

package com.totec.trading.core.pooled.bookentry


/*class BookEntryArray64 private constructor() {
	private var arr = Array(64) { BookEntry.createDefault() }
	
	operator fun get(index: Int) = arr[index]
	
	operator fun set(index: Int, entry: BookEntry) {
		arr[index] = entry
	}
	
	companion object : FastPool<BookEntryArray64>() {
		override var arr = Array(16) { BookEntryArray64() }
		
		override fun createDefault() = BookEntryArray64()
		
		override fun createArray(newSize: Int) = Array(newSize) { createDefault() }
	}
}*/
