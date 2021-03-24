/**
 * Created by ndrsh on 23.11.20
 */

package com.totec.trading.core.engines

import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.instrument.book.ops.BookOp

/**
 * Handle reference count of [BookOp] objects. This is mandatory, failing to do so will crash the JVM.
 */
interface BookEngine : Engine, Flushable {
	
	fun addAskOp(bookOp: BookOp)
	
	fun addBidOp(bookOp: BookOp)
	
	fun addBook(book: Book)
}
