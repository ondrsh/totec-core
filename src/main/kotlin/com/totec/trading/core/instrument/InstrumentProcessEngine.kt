/**
 * Created by ndrsh on 7/8/20
 */

package com.totec.trading.core.instrument

/**
 * Method [flush] has to be called after any changes are made EXCEPT when doing info updates or adding
 * lags. So process instrument updates and lags immediately or you add the underlying objects. Also handle reference
 * counts of [BookOp] and [Trade]. You **need** to decrease their count once you don't use them anymore,
 * otherwise the JVM will crash.
 *
 * See documentation at [InstrumentProcessEngines].
 *//*

interface InstrumentProcessEngine {
	
	var exchange: Exchange
	var instrumentInfo: InstrumentInfo
	
	fun addAskOp(bookOp: BookOp) // verteilt
	
	fun addBidOp(bookOp: BookOp) // verteilt
	
	fun addTrade(trade: Trade) // verteilt
	
	fun addBook(book: Book) // verteilt
	
	fun addInfoFull(instrumentInfo: InstrumentInfo) // verteilt
	
	*/
/**
	 * Here, after this call, 'changed' will be reset, so for example when using ChronicleWriter,
	 * write immediately and don't do anything at flush.
	 *//*

	fun addInfoUpdate(instrumentInfo: InstrumentInfo) // verteilt
	
	fun addBookLag(lag: Long, timestamp: Long) // verteilt
	
	fun addTradeLag(lag: Long, timestamp: Long) // verteilt
	
	fun addInstrumentLag(lag: Long, timestamp: Long) // verteilt
	
	fun deleteLiquidation(liquidation: Liquidation) // verteilt
	
	*/
/** This is either an insert or an update. *//*

	fun processLiquidation(liquidation: Liquidation) // verteilt
	
	fun flush(timestamp: Long)
}
*/
