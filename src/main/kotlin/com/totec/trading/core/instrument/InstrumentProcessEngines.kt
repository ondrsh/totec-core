/**
 * Created by ndrsh on 8/26/20
 */

// @file:Suppress("UNCHECKED_CAST")

package com.totec.trading.core.instrument

import com.totec.trading.core.engines.*
import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.instrument.book.Trade
import com.totec.trading.core.instrument.book.ops.BookOp
import javax.inject.Inject

class InstrumentProcessEngines @Inject constructor(enginesList: List<@JvmSuppressWildcards Engine>) {
	
	val bookEngines = enginesList.filterIsInstance<BookEngine>()
	val tradeEngines = enginesList.filterIsInstance<TradeEngine>()
	val lagEngines = enginesList.filterIsInstance<LagEngine>()
	val liquidationEngines = enginesList.filterIsInstance<LiquidationEngine>()
	val infoEngines = enginesList.filterIsInstance<InfoEngine>()
	val flushable = enginesList.filterIsInstance<Flushable>()
	
	// For every engine, increase the count. Then, decrease our count
	fun addAskOp(bookOp: BookOp) {
		for (ipce in bookEngines) {
			bookOp.incCount()
			ipce.addAskOp(bookOp)
		}
		bookOp.decCount()
	}
	
	// For every engine, increase the count. Then, decrease our count
	fun addBidOp(bookOp: BookOp) {
		for (ipce in bookEngines) {
			bookOp.incCount()
			ipce.addBidOp(bookOp)
		}
		bookOp.decCount()
	}
	
	// For every engine, increase the count. Then, decrease our count
	fun addTrade(trade: Trade) {
		for (ipce in tradeEngines) {
			trade.incCount()
			ipce.addTrade(trade)
		}
		trade.decCount()
	}
	
	fun addBook(book: Book) {
		for (ipce in bookEngines) {
			ipce.addBook(book)
		}
	}
	
	fun addTradeLag(lag: Long, timestamp: Long) {
		for (ipce in lagEngines) {
			ipce.addTradeLag(lag, timestamp)
		}
	}
	
	fun addBookLag(lag: Long, timestamp: Long) {
		for (ipce in lagEngines) {
			ipce.addBookLag(lag, timestamp)
		}
	}
	
	fun addInstrumentLag(lag: Long, timestamp: Long) {
		for (ipce in lagEngines) {
			ipce.addInstrumentLag(lag, timestamp)
		}
	}
	
	fun addInfoFull(instrumentInfo: InstrumentInfo) {
		for (ipce in infoEngines) {
			ipce.addInfoFull(instrumentInfo)
		}
	}
	
	fun addInfoUpdate(instrumentInfo: InstrumentInfo) {
		for (ipce in infoEngines) {
			ipce.addInfoUpdate(instrumentInfo)
		}
	}
	
	fun deleteLiquidation(liquidation: Liquidation) {
		for (ipce in liquidationEngines) {
			ipce.deleteLiquidation(liquidation)
		}
	}
	
	fun processLiquidation(liquidation: Liquidation) {
		for (ipce in liquidationEngines) {
			ipce.insertLiquidation(liquidation)
		}
	}
	
	fun flush(timestamp: Long) {
		for (ipce in flushable) {
			ipce.flush(timestamp)
		}
	}
}
