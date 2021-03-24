/**
 * Created by ndrsh on 7/7/20
 */

package com.totec.trading.core.instrument.currencies


class CurrencyPair(var base: Currency,
                   var quote: Currency,
                   var position: Currency = base) {
	companion object {
		fun invalid() = CurrencyPair(Currency.INVALID, Currency.INVALID)
	}
	
	override fun equals(other: Any?): Boolean {
		if (other !is CurrencyPair) return false
		if (base != other.base || quote != other.quote) return false
		return true
	}
	
	override fun hashCode(): Int {
		var hash = 7
		hash = 31 * hash + base.hashCode()
		hash = 31 * hash + quote.hashCode()
		hash = 31 * hash + position.hashCode()
		return hash
	}
}
