/**
 * Created by ndrsh on 8/4/20
 */

package com.totec.trading.core.write.chronicle

import com.totec.trading.core.instrument.EngineComponent
import dagger.BindsInstance
import dagger.Component
import net.openhft.chronicle.queue.ChronicleQueue

@Component(modules = [ChronicleWriterModule::class])
interface ChronicleWriterComponent : EngineComponent {
	
	@Component.Factory
	interface Factory {
		fun create(@BindsInstance chronicleQueue: ChronicleQueue): ChronicleWriterComponent
	}
}
