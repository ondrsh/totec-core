/**
 * Created by ndrsh on 09.11.20
 */

package com.totec.trading.core.instrument

import com.totec.parser.json.ObjectReader
import com.totec.trading.core.exchanges.SymbolConverter

interface InfoDeserializer : ObjectReader<InstrumentInfo> {
	val currenciesAllowed: CurrenciesAllowed
	val symbolConverter: SymbolConverter
	
	/**
	 * Care that you [currenciesAllowed] might get changed.
	 */
	fun InstrumentInfo.passesFilter(): Boolean
}
