/**
 * Created by ndrsh on 7/21/20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.engines.Engine
import dagger.Module
import dagger.Provides

@Module
class DummyInstrumentProcessEngineModule {
	@Provides
	fun provideInstrumentProcessEngine(dummyInstrumentProcessEngine: DummyInstrumentProcessEngine): Engine = dummyInstrumentProcessEngine
}
