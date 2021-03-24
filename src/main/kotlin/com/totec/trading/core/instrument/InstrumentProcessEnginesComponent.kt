/**
 * Created by ndrsh on 8/26/20
 */

package com.totec.trading.core.instrument

import dagger.BindsInstance
import dagger.Component

@Component(modules = [InstrumentProcessEnginesModule::class])
interface InstrumentProcessEnginesComponent {
	fun instrumentProcessEngines(): InstrumentProcessEngines
	
	@Component.Factory
	interface Factory {
		fun create(@BindsInstance engineComponents: List<EngineComponent>): InstrumentProcessEnginesComponent
	}
}
