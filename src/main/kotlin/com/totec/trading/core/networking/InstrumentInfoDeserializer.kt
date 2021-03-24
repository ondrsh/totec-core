/**
 * Created by ndrsh on 7/7/20
 */

package com.totec.trading.core.networking

import com.totec.trading.core.exchanges.SymbolConverter
import com.totec.trading.core.instrument.InstrumentInfo
import com.totec.trading.core.instrument.CurrenciesAllowed
import com.totec.trading.core.utils.dsljson.ObjectReader

interface InstrumentInfoDeserializer : ObjectReader<InstrumentInfo> {
	val currenciesAllowed: CurrenciesAllowed
	val symbolConverter: SymbolConverter
	
	/**
	 * Care that you [currenciesAllowed] might get changed.
	 */
	fun InstrumentInfo.passesFilter(): Boolean
}
