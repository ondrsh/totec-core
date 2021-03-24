/**
 * Created by ndrsh on 7/11/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.engines.*
import com.totec.trading.core.exchanges.Exchange
import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.instrument.book.Trade
import com.totec.trading.core.instrument.book.ops.BookOp
import java.util.*
import javax.inject.Inject

class DummyInstrumentProcessEngine @Inject constructor() : BookEngine, InfoEngine, TradeEngine, LagEngine, LiquidationEngine {
	
	override lateinit var exchange: Exchange
	override lateinit var instrumentInfo: InstrumentInfo
	
	val trades = ArrayDeque<Trade>()
	var tradesReceived = 0L
	val bidOps = ArrayDeque<BookOp>()
	var bidsReceived = 0L
	val askOps = ArrayDeque<BookOp>()
	var asksReceived = 0L
	val books = ArrayDeque<Book>()
	var booksReceived = 0L
	
	override fun addBidOp(bookOp: BookOp) {
		bidOps.add(bookOp)
		bidsReceived++
	}
	
	override fun addAskOp(bookOp: BookOp) {
		askOps.add(bookOp)
		asksReceived++
	}
	
	override fun addTrade(trade: Trade) {
		trades.add(trade)
		tradesReceived++
	}
	
	override fun addBook(book: Book) {
		books.add(book)
		booksReceived++
	}
	
	override fun addInfoFull(instrumentInfo: InstrumentInfo) {
		// TODO("Not yet implemented")
	}
	
	override fun addInfoUpdate(instrumentInfo: InstrumentInfo) {
		// TODO("Not yet implemented")
	}
	
	override fun addBookLag(lag: Long, timestamp: Long) {
		// TODO("Not yet implemented")
	}
	
	override fun addTradeLag(lag: Long, timestamp: Long) {
		// TODO("Not yet implemented")
	}
	
	override fun addInstrumentLag(lag: Long, timestamp: Long) {
		// TODO("Not yet implemented")
	}
	
	override fun insertLiquidation(liquidation: Liquidation) {
		// TODO("Not yet implemented")
	}
	
	override fun deleteLiquidation(liquidation: Liquidation) {
		TODO("Not yet implemented")
	}
	
	override fun flush(timestamp: Long) {
		trades.clear()
		bidOps.clear()
		askOps.clear()
		books.clear()
		tradesReceived = 0
		bidsReceived = 0
		asksReceived = 0
		booksReceived = 0
	}
}
