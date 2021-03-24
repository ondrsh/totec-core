/**
 * Created by ndrsh on 7/11/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.instrument.currencies.Currency
import com.totec.trading.core.instrument.currencies.CurrencyPair
import net.openhft.chronicle.bytes.BytesIn
import net.openhft.chronicle.bytes.BytesOut

interface InstrumentInfo {
	var timestamp: Long
	var symbol: String
	var pair: CurrencyPair
	val lotSize: Size
	val tickSize: Size
	
	fun replace(otherInfo: InstrumentInfo) {
		symbol = otherInfo.symbol
		pair = otherInfo.pair
		if (otherInfo.lotSize.isInitialized) lotSize.initializeWith(otherInfo.lotSize.value)
		if (otherInfo.tickSize.isInitialized) tickSize.initializeWith(otherInfo.tickSize.value)
		timestamp = otherInfo.timestamp
	}
	
	fun update(otherInfo: InstrumentInfo): Boolean
	
	/**
	 * Call this function after you have processed the update.
	 * Will reset the indicators about which fields have been changed and which have not been changed.
	 */
	fun cleanAfterUpdate()
	
	/**
	 * Write the update of this [InstrumentInfo] to the Chronicle Appender. Last write should be an 8bit called "end".
	 */
	fun writeInfoUpdate(bytes: BytesOut<*>)
	
	/**
	 * Write the full [InstrumentInfo] to the Chronicle Appender. Note that the timestamp has already been written.
	 * So just write the other info. Writing does not have to be backward compatible, **but reading has to be**, so you can
	 * delete something from the writing logic, but never from the reading logic. At the end, add an 8bit called "end"
	 * so we know when to stop reading.
	 */
	fun writeInfoFull(bytes: BytesOut<*>)
	
	/**
	 * Read from the queue to this [InstrumentInfo]. Don't ever delete logic there because it has to be backward compatible.
	 * Return from this function when you read an 8bit called "end". This can be used both for full InstrumentInfos as well as updates.
	 */
	fun readInfo(bytes: BytesIn<*>, readTimestamp: Long)
	
	/** Calculate the lag between the real observed [timestamp] and the timestamp that is delivered in the live feed (if it exists). */
	fun calculateLag(): Long
	
	fun BytesIn<*>.setSymbol() {
		symbol = read8bit() as String
	}
	
	fun BytesIn<*>.setPair() {
		pair = CurrencyPair(base = readEnum(Currency::class.java),
		                    quote = readEnum(Currency::class.java),
		                    position = readEnum(Currency::class.java))
	}
	
	/**
	 * Used for IntrospectionReadFeed. Don't read the symbol in this function, it is read by the reader.
	 */
	fun appendTo(stringBuilder: StringBuilder)
	
}
