/**
 * Created by ndrsh on 6/1/20
 */

package com.totec.trading.core.exchanges

import com.totec.trading.core.instrument.InfoDeserializer
import com.totec.trading.core.instrument.Instrument
import com.totec.trading.core.instrument.InstrumentComponent
import com.totec.trading.core.instrument.InstrumentInfo
import com.totec.trading.core.networking.InstrumentInfoDeserializer
import com.totec.trading.core.networking.RestClient

interface Exchange {
	val name: Name
	val restClient: RestClient
	val instruments: MutableMap<String, Instrument>
	val instrumentComponent: InstrumentComponent
	val infoDeserializer: InfoDeserializer
	val instrumentInfoDeserializer: InstrumentInfoDeserializer
	val symbolConverter: SymbolConverter
	
	enum class Name {
		BINANCE { override val id = 1.toByte() },
		BITHUMB { override val id = 2.toByte() },
		BITFINEX { override val id = 3.toByte() },
		BITMEX { override val id = 4.toByte() },
		BITSTAMP { override val id = 5.toByte() },
		COINBASE { override val id = 6.toByte() },
		GEMINI { override val id = 7.toByte() },
		KRAKEN { override val id = 8.toByte() },
		// OKEX { override val id = 9.toByte() },
		POLONIEX { override val id = 10.toByte() };
		
		abstract val id: Byte
		
		companion object {
			fun getExchangeName(id: Byte) = when (id) {
				1.toByte() -> BINANCE
				2.toByte() -> BITHUMB
				3.toByte() -> BITFINEX
				4.toByte() -> BITMEX
				5.toByte() -> BITSTAMP
				6.toByte() -> COINBASE
				7.toByte() -> GEMINI
				8.toByte() -> KRAKEN
				9.toByte() -> POLONIEX
				else -> throw RuntimeException("Couldn't find exchange with id $id")
			}
		}
	}
}

operator fun Exchange.contains(symbol: String): Boolean = instruments.containsKey(symbol)

fun Exchange.addInstrument(instrument: Instrument) {
	instruments[instrument.info.symbol] = instrument
}

fun Exchange.createAndAddInstrument(startingInfo: InstrumentInfo): Instrument = instrumentComponent.instrument().apply {
	replaceInfo(startingInfo)
}.also { addInstrument(it) }
