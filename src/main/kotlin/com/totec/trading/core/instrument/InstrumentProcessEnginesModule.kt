/**
 * Created by ndrsh on 8/26/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.engines.Engine
import dagger.Module
import dagger.Provides

@Module
class InstrumentProcessEnginesModule {
	@Provides fun engines(engineComponents: List<EngineComponent>): List<Engine> {
		return engineComponents.map { it.engine() }
	}
}
