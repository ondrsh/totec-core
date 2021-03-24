/**
 * Created by ndrsh on 7/18/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.instrument.currencies.Currency
import com.totec.trading.core.instrument.currencies.CurrencyPair

/**
 * You either filter with [baseCurrencies] or with [pairs]. If one is set, the other one has to be empty.
 */
class CurrenciesAllowed(private val baseCurrencies: Set<Currency> = setOf(),
                        private val pairs: Set<CurrencyPair> = setOf()) {
    
    operator fun contains(currency: Currency): Boolean {
        if (baseCurrencies.isEmpty()) return true
        if (currency in baseCurrencies) return true
        return false
    }
    
    operator fun contains(pair: CurrencyPair): Boolean {
        if (pairs.isEmpty()) return true
        if (pair in pairs) return true
        return false
    }
}
