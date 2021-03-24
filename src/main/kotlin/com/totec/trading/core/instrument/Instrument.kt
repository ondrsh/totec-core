/**
 * Created by ndrsh on 5/31/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.exchanges.Exchange
import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.instrument.book.Side
import com.totec.trading.core.instrument.book.Trade
import com.totec.trading.core.instrument.book.ops.BookOp

/**
 * Never use [engines] directly. I.e., if you want to to process a [BookOp], call one of the
 * corresponding functions on this [Instrument] interface.
 */
interface Instrument {
	val exchange: Exchange
	val engines: InstrumentProcessEngines
	val info: InstrumentInfo
	val book: Book
	
	fun processTrade(trade: Trade) = engines.addTrade(trade)
	fun processBook(newBook: Book)
	fun processInsert(insert: BookOp, side: Side): Boolean = book[side].processInsert(insert)
	fun processChange(change: BookOp, side: Side): Boolean = book[side].processChange(change)
	fun processDelete(delete: BookOp, side: Side): Boolean = book[side].processDelete(delete)
	fun addBookLag(lag: Long, timestamp: Long) = engines.addBookLag(lag, timestamp)
	fun addTradeLag(lag: Long, timestamp: Long) = engines.addTradeLag(lag, timestamp)
	fun addInstrumentLag(lag: Long, timestamp: Long) = engines.addInstrumentLag(lag, timestamp)
	fun deleteLiquidation(liquidation: Liquidation) = engines.deleteLiquidation(liquidation)
	fun processLiquidation(liquidation: Liquidation) = engines.processLiquidation(liquidation)
	fun flush(timestamp: Long) = engines.flush(timestamp)
	
	fun updateInfo(otherInfo: InstrumentInfo): Boolean {
		val result = info.update(otherInfo)
		if (result) engines.addInfoUpdate(otherInfo)
		info.cleanAfterUpdate()
		return result
	}
	
	fun replaceInfo(otherInfo: InstrumentInfo) {
		info.replace(otherInfo)
		engines.addInfoFull(otherInfo)
	}
}
