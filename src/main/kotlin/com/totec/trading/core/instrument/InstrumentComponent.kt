/**
 * Created by ndrsh on 6/27/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.instrument.book.Book
import com.totec.trading.core.instrument.book.bookside.Asks
import com.totec.trading.core.instrument.book.bookside.Bids

interface InstrumentComponent {
	fun instrument(): Instrument
	fun info(): InstrumentInfo
	fun instrumentProcessEngines(): InstrumentProcessEngines
	fun book(): Book
	fun asks(): Asks
	fun bids(): Bids
}
