/**
 * Created by ndrsh on 23.11.20
 */

package com.totec.trading.core.engines

import com.totec.trading.core.instrument.Liquidation

/** Process these immediately. Not flushable */
interface LiquidationEngine {
	
	fun deleteLiquidation(liquidation: Liquidation)
	
	/** This is either an insert or an update. */
	fun insertLiquidation(liquidation: Liquidation)
	
	/** This is either an insert or an update. */
}
