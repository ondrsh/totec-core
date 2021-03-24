/**
 * Created by ndrsh on 7/10/20
 */

package com.totec.trading.core.write.chronicle

import com.totec.trading.core.engines.*
import com.totec.trading.core.exchanges.Exchange
import com.totec.trading.core.instrument.InstrumentInfo
import com.totec.trading.core.instrument.Lag
import com.totec.trading.core.instrument.Liquidation
import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.instrument.book.BookEntry
import com.totec.trading.core.instrument.book.Side
import com.totec.trading.core.instrument.book.Trade
import com.totec.trading.core.instrument.book.bookside.BookSide
import com.totec.trading.core.instrument.book.ops.BookOp
import com.totec.trading.core.instrument.book.ops.OpType
import com.totec.trading.core.utils.logger
import net.openhft.chronicle.bytes.BytesOut
import net.openhft.chronicle.queue.ChronicleQueue
import net.openhft.chronicle.queue.ExcerptAppender
import java.io.File
import java.time.Instant
import javax.inject.Inject

open class ChronicleWriter @Inject constructor(chronicleQueue: ChronicleQueue) :
	InfoEngine,
	BookEngine,
	LagEngine,
	TradeEngine,
	LiquidationEngine {
	
	override lateinit var exchange: Exchange
	override lateinit var instrumentInfo: InstrumentInfo
	val appender: ExcerptAppender = chronicleQueue.acquireAppender()
	
	// general message
	var type: WriteType = WriteType.Nothing
	
	// ops
	val bidOps: MutableList<BookOp> = arrayListOf()
	val askOps: MutableList<BookOp> = arrayListOf()
	
	// trades
	val buyTrades: MutableList<Trade> = arrayListOf()
	val sellTrades: MutableList<Trade> = arrayListOf()
	
	// book
	lateinit var book: Book
	
	// liquidations
	val liquidationsInserts = mutableListOf<Liquidation>()
	val liquidationsDeletes = mutableListOf<Liquidation>()
	
	// lags
	val bookLag = Lag()
	val tradeLag = Lag()
	val instrumentLag = Lag()
	
	override fun addBidOp(bookOp: BookOp) {
		// to prevent that inserts and changes are written together
		if (type.isOp() && type != bookOp.type.toWriteType()) flush(book.timestamp)
		bidOps.add(bookOp)
		type = bookOp.type.toWriteType()
		// lastTimestamp = bookOp.timestamp
	}
	
	override fun addAskOp(bookOp: BookOp) {
		// to prevent that inserts and changes are written together
		if (type.isOp() && type != bookOp.type.toWriteType()) flush(bookOp.timestamp)
		askOps.add(bookOp)
		type = bookOp.type.toWriteType()
		// lastTimestamp = bookOp.timestamp
	}
	
	override fun addTrade(trade: Trade) {
		if (trade.initiatingSide == Side.Buy) buyTrades.add(trade)
		if (trade.initiatingSide == Side.Sell) sellTrades.add(trade)
		type = WriteType.Trade
		// lastTimestamp = trade.timestamp
	}
	
	override fun addBook(book: Book) {
		this.book = book
		type = WriteType.Book
		// lastTimestamp = book.timestamp
	}
	
	override fun addInfoFull(instrumentInfo: InstrumentInfo) {
		type = WriteType.InfoFull
	}
	
	override fun addInfoUpdate(instrumentInfo: InstrumentInfo) {
		type = WriteType.InfoUpdate
		writeInfoUpdate()
	}
	
	override fun addBookLag(lag: Long, timestamp: Long) {
		bookLag.lagList.add(lag)
	}
	
	override fun addTradeLag(lag: Long, timestamp: Long) {
		tradeLag.lagList.add(lag)
	}
	
	override fun addInstrumentLag(lag: Long, timestamp: Long) {
		instrumentLag.lagList.add(lag)
	}
	
	override fun insertLiquidation(liquidation: Liquidation) {
		type = WriteType.LiquidationInsert
		liquidationsInserts.add(liquidation)
	}
	
	override fun deleteLiquidation(liquidation: Liquidation) {
		type = WriteType.LiquidationDelete
		liquidationsDeletes.add(liquidation)
	}
	
	override fun flush(timestamp: Long) {
		// if (instrumentInfo.symbol != "XBTUSD") return
		when (type) {
			WriteType.OpInsert, WriteType.OpChange, WriteType.OpDelete -> {
				if (bookLag.lagList.isNotEmpty()) writeBookLag(timestamp)
				processOps(timestamp)
			}
			WriteType.Trade                                          -> {
				if (tradeLag.lagList.isNotEmpty()) writeTradeLag(timestamp)
				processTrades(timestamp)
			}
			WriteType.Book                                           -> {
				if (bookLag.lagList.isNotEmpty()) writeBookLag(timestamp)
				writeBook(timestamp)
			}
			WriteType.InfoUpdate                                     -> { // we are immediately writing it, we don't wait for a flush.
				if (instrumentLag.lagList.isNotEmpty()) writeInstrumentLag(timestamp)
			}
			WriteType.InfoFull                                       -> writeInfoFull()
			WriteType.LiquidationInsert -> processLiquidationsInserts()
			WriteType.LiquidationDelete -> processLiquidationsDeletes()
			WriteType.Lag                                            -> {
			} // we don't flush them because we process them at the same time as we add trades and ops
			WriteType.Nothing                                        -> {
				logger.warn("Writing Nothing at Chroniclewriter at instrument ${instrumentInfo.symbol}. Timestamp $timestamp")
			}
		}
		
		// println("wrote $type on ${instrumentInfo.symbol}")
		debugFile.appendText("${Instant.now()} - $timestamp - ${instrumentInfo.symbol} wrote $type\n")
		type = WriteType.Nothing
	}
	
	val debugFile = File("bitmexFeedSamples/14.11.2020.writer")
	// TODO remove this
	
	fun writeBookLag(timestamp: Long) {
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((lagOffset + 1).toShort())
			write8bit(instrumentInfo.symbol)
			writeLong(bookLag.lagList.getAverageAndRemoveAll())
		}
		bookLag.writtenTimestamp = timestamp
		println("wrote BookLag on ${instrumentInfo.symbol}")
	}
	
	fun writeTradeLag(timestamp: Long) {
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((lagOffset + 2).toShort())
			write8bit(instrumentInfo.symbol)
			writeLong(tradeLag.lagList.getAverageAndRemoveAll())
		}
		tradeLag.writtenTimestamp = timestamp
		debugFile.appendText("${Instant.now()} - $timestamp - ${instrumentInfo.symbol} wrote TradeLag\n")
		// println("wrote TradeLag on ${instrumentInfo.symbol}")
	}
	
	fun writeInstrumentLag(timestamp: Long) {
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((lagOffset + 3).toShort())
			write8bit(instrumentInfo.symbol)
			writeLong(instrumentLag.lagList.getAverageAndRemoveAll())
		}
		instrumentLag.writtenTimestamp = timestamp
		debugFile.appendText("${Instant.now()} - $timestamp - ${instrumentInfo.symbol} wrote InstrumentLag\n")
		// println("wrote InstrumentLag on ${instrumentInfo.symbol}")
	}
	
	fun processOps(timestamp: Long) {
		val bidOpsSize = bidOps.size
		val askOpsSize = askOps.size
		
		if (bidOpsSize > 0) {
			if (askOpsSize == 0) {
				if (bidOps.size == 1) {
					// SingleBid
					processOneSidedOps(bidOps, 1, Side.Buy.offset, timestamp)
				} else {
					// MultipleBids
					processOneSidedOps(bidOps, bidOpsSize, Side.Buy.offset + multipleOffset, timestamp)
				}
			} else {
				// MixedOps, offset == 34
				processMixedOps(bidOpsSize, askOpsSize, timestamp)
			}
		} else if (askOpsSize == 1) {
			// SingleAsk
			processOneSidedOps(askOps, 1, Side.Sell.offset, timestamp)
		} else if (askOpsSize > 1) {
			// MultipleAsks
			processOneSidedOps(askOps, askOpsSize, Side.Sell.offset + multipleOffset, timestamp)
		} else {
			throw RuntimeException("ChronicleWriter doesn't know what to do, type is $type but bidsSize is $bidOpsSize and askSize is $askOpsSize")
		}
	}
	
	fun processTrades(timestamp: Long) {
		val buyTradesSize = buyTrades.size
		val sellTradesSize = sellTrades.size
		if (buyTradesSize > 0) {
			if (sellTradesSize == 0) {
				if (buyTradesSize == 1) {
					// Single Buy
					processOneSidedTrades(buyTrades, 1, 1, timestamp)
				} else {
					// Multiple Buys
					processOneSidedTrades(buyTrades, buyTradesSize, 2, timestamp)
				}
			} else {
				// Mixed Trades, offset == 34
				writeMixedTrades(buyTradesSize, sellTradesSize, timestamp)
			}
		} else if (sellTradesSize == 1) {
			// Single Sell
			processOneSidedTrades(sellTrades, 1, Side.Sell.offset, timestamp)
		} else if (sellTradesSize > 1) {
			// Multiple Sells
			processOneSidedTrades(sellTrades, sellTradesSize, 18, timestamp)
		}
	}
	
	fun processOneSidedOps(opList: MutableList<BookOp>, size: Int, typeOffset: Int, timestamp: Long) {
		val firstOp = opList[0]
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((typeOffset + firstOp.type.offset).toShort())
			write8bit(instrumentInfo.symbol)
			if (type == WriteType.OpDelete) writeOpPrices(opList, size)
			else writeOpPricesAndAmounts(opList, size) // WriteType = Insert ||  Change
		}
	}
	
	fun processMixedOps(bidsSize: Int, asksSize: Int, timestamp: Long) {
		val typeOffset = mixedOffset
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((typeOffset + bidOps[0].type.offset).toShort())
			write8bit(instrumentInfo.symbol)
			if (type == WriteType.OpDelete) {
				writeInt(bidsSize)
				for (op in bidOps) writeOpPrice(op)
				bidOps.clear()
				
				writeInt(asksSize)
				for (op in askOps) writeOpPrice(op)
				askOps.clear()
			} else { // WriteType = Insert ||  Change
				writeInt(bidsSize)
				for (op in bidOps) writeOpPriceAndAmount(op)
				bidOps.clear()
				
				writeInt(asksSize)
				for (op in askOps) writeOpPriceAndAmount(op)
				askOps.clear()
			}
		}
	}
	
	fun processOneSidedTrades(tradeList: MutableList<Trade>, size: Int, typeOffset: Int, timestamp: Long) {
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((typeOffset + 100).toShort())
			write8bit(instrumentInfo.symbol)
			if (size > 1) writeInt(size)
			writeTrades(tradeList)
			tradeList.clear()
		}
	}
	
	fun writeMixedTrades(buyTradesSize: Int, sellTradesSize: Int, timestamp: Long) {
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort((tradeOffset + mixedOffset).toShort())
			write8bit(instrumentInfo.symbol)
			
			// write buy trades
			writeInt(buyTradesSize)
			for (trade in buyTrades) writeTrade(trade)
			buyTrades.clear()
			
			// write sell trades
			writeInt(sellTradesSize)
			for (trade in sellTrades) writeTrade(trade)
			sellTrades.clear()
		}
	}
	
	fun writeBook(timestamp: Long) {
		appender.writeBytesRaw {
			writeLong(timestamp)
			writeByte(exchange.name.id)
			writeShort(bookOffset.toShort())
			write8bit(instrumentInfo.symbol)
			writeBookSide(book.bids)
			writeBookSide(book.asks)
		}
	}
	
	fun BytesOut<*>.writeBookSide(bookSide: BookSide) {
		writeLong(bookSide.lastUpdated)
		writeInt(bookSide.set.size)
		for (entry in bookSide.set) {
			writeEntry(entry)
		}
	}
	
	fun BytesOut<*>.writeEntry(entry: BookEntry) {
		writeDouble(entry.price)
		writeDouble(entry.amount)
		writeLong(entry.timestamp)
	}
	
	fun writeInfoUpdate() {
		appender.writeBytesRaw {
			writeLong(instrumentInfo.timestamp)
			writeByte(exchange.name.id)
			writeShort((infoUpdateOffset).toShort())
			write8bit(instrumentInfo.symbol)
			instrumentInfo.writeInfoUpdate(this)
		}
	}
	
	fun writeInfoFull() {
		appender.writeBytesRaw {
			writeLong(instrumentInfo.timestamp)
			writeByte(exchange.name.id)
			writeShort(infoFullOffset.toShort())
			write8bit(instrumentInfo.symbol)
			instrumentInfo.writeInfoFull(this)
		}
	}
	
	fun processLiquidationsInserts() {
		appender.writeBytesRaw {
			writeLong(instrumentInfo.timestamp)
			writeByte(exchange.name.id)
			writeShort(liquidationOffset.toShort())
			write8bit(instrumentInfo.symbol)
			writeInt(liquidationsInserts.size)
			for (liquidation in liquidationsInserts) writeLiquidation(liquidation)
			liquidationsInserts.clear()
		}
	}
	
	fun processLiquidationsDeletes() {
		appender.writeBytesRaw {
			writeLong(instrumentInfo.timestamp)
			writeByte(exchange.name.id)
			writeShort((liquidationOffset+1).toShort())
			write8bit(instrumentInfo.symbol)
			writeInt(liquidationsDeletes.size)
			for (liquidation in liquidationsDeletes) writeLiquidation(liquidation)
			liquidationsDeletes.clear()
		}
	}
	fun BytesOut<*>.writeLiquidations() {
	}
	
	fun BytesOut<*>.writeLiquidation(liquidation: Liquidation) {
		write8bit(liquidation.orderID)
		writeEnum(liquidation.side)
		writeDouble(liquidation.price)
		writeDouble(liquidation.amount)
	}
	
	fun BytesOut<*>.writeOpPricesAndAmounts(opList: MutableList<BookOp>, size: Int) {
		if (size > 1) writeInt(size)
		for (op in opList) writeOpPriceAndAmount(op)
		opList.clear()
	}
	
	private fun BytesOut<*>.writeOpPriceAndAmount(op: BookOp) {
		writeDouble(op.price)
		writeDouble(op.amount)
		op.decCount()
	}
	
	fun BytesOut<*>.writeOpPrices(opList: MutableList<BookOp>, size: Int) {
		if (size > 1) writeInt(size)
		for (op in opList) writeOpPrice(op)
		opList.clear()
	}
	
	private fun BytesOut<*>.writeOpPrice(op: BookOp) {
		writeDouble(op.price)
		op.decCount()
	}
	
	fun BytesOut<*>.writeTrades(tradeList: MutableList<Trade>) {
		for (trade in tradeList) writeTrade(trade)
	}
	
	fun BytesOut<*>.writeTrade(trade: Trade) {
		writeDouble(trade.price)
		writeDouble(trade.amount)
		trade.decCount()
	}
	
	enum class WriteType {
		Trade, OpInsert, OpChange, OpDelete, Book, LiquidationInsert, LiquidationDelete, InfoUpdate, InfoFull, Lag, Nothing;
		
		fun isOp(): Boolean {
			return when (this) {
				OpInsert, OpChange, OpDelete -> true
				else                         -> false
			}
		}
	}
	
	val Exchange.offset
		get() = name.id.toInt()
}

fun OpType.toWriteType(): ChronicleWriter.WriteType = when (this) {
	OpType.INSERT -> ChronicleWriter.WriteType.OpInsert
	OpType.CHANGE -> ChronicleWriter.WriteType.OpChange
	OpType.DELETE -> ChronicleWriter.WriteType.OpDelete
}


/** Write lags if 1) there are lags present and 2) more than 2s have passed since last write */
fun Lag.shouldBeWritten(timestamp: Long): Boolean = lagList.isNotEmpty() && timestamp - writtenTimestamp > 2000L
