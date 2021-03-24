/**
 * Created by ndrsh on 8/31/20
 */

package com.totec.trading.core.exchanges

class ExchangesAllowed(val exchanges: Set<Exchange.Name> = Exchange.Name.values().toSet()) {
	
	operator fun contains(exchangeName: Exchange.Name) = exchangeName in exchanges
}
