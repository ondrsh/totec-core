/**
 * Created by ndrsh on 7/7/20
 */

package com.totec.trading.core.exchanges

import com.totec.trading.core.instrument.InstrumentInfo
import com.totec.trading.core.instrument.currencies.Currency
import com.totec.trading.core.instrument.currencies.CurrencyPair

interface SymbolConverter {
	fun getCurrencyPair(info: InstrumentInfo): CurrencyPair
	fun getCurrency(symbol: String): Currency
}
