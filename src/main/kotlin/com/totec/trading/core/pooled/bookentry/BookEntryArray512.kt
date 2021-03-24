/**
 * Created by ndrsh on 5/30/20
 */

package com.totec.trading.core.pooled.bookentry


/*class BookEntryArray512 private constructor() {
	private var arr = Array(512) { BookEntry.createDefault() }
	
	operator fun get(index: Int) = arr[index]
	
	operator fun set(index: Int, entry: BookEntry) {
		arr[index] = entry
	}
	
	companion object : FastPool<BookEntryArray512>() {
		override var arr = Array(4) { BookEntryArray512() }
		
		override fun createDefault() = BookEntryArray512()
		
		override fun createArray(newSize: Int) = Array(newSize) { createDefault() }
	}
}*/
