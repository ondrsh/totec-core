/**
 * Created by ndrsh on 14.06.20
 */

package com.totec.trading.core.exchanges

interface ExchangeComponent {
	fun exchange(): Exchange
	// fun symbolConverter(): SymbolConverter
}
