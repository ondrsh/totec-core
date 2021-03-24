/**
 * Created by ndrsh on 7/10/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.engines.Engine

interface EngineComponent {
	fun engine(): Engine
}
