/**
 * Created by ndrsh on 6/2/20
 */

package com.totec.trading.core.instrument.book.bookside

import com.totec.trading.core.instrument.InstrumentProcessEngines
import com.totec.trading.core.instrument.book.BookEntry
import com.totec.trading.core.instrument.book.Side
import com.totec.trading.core.instrument.book.ops.BookOp

interface BookSide {
	val side: Side
	var lastUpdated: Long
	var map: MutableMap<Double, BookEntry>
	var set: MutableSet<BookEntry>
	var engines: InstrumentProcessEngines
	/** If successful (op has been passed to engines) return true, if unsuccessful, decrease reference count and return false */
	fun processInsert(insert: BookOp): Boolean
	/** If successful (op has been passed to engines) return true, if unsuccessful, decrease reference count and return false */
	fun processChange(change: BookOp): Boolean
	/** If successful (op has been passed to engines) return true, if unsuccessful, decrease reference count and return false */
	fun processDelete(delete: BookOp): Boolean
	fun InstrumentProcessEngines.addOp(op: BookOp)
}
