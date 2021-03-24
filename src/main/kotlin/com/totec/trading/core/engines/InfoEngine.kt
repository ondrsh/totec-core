/**
 * Created by ndrsh on 23.11.20
 */

package com.totec.trading.core.engines

import com.totec.trading.core.exchanges.Exchange
import com.totec.trading.core.instrument.InstrumentInfo

/**
 * Process these immediately. This is not flushable.
 */
interface InfoEngine: Engine {
	
	var exchange: Exchange
	var instrumentInfo: InstrumentInfo
	
	fun addInfoFull(instrumentInfo: InstrumentInfo)
	
	/**
	 * Here, after this call, 'changed' will be reset, so for example when using ChronicleWriter,
	 * write immediately and don't wait for a flush.
	 */
	fun addInfoUpdate(instrumentInfo: InstrumentInfo)
}
