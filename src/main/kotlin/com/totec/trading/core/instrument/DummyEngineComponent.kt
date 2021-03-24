/**
 * Created by ndrsh on 7/11/20
 */

package com.totec.trading.core.instrument

import dagger.Component

@Component(modules = [DummyInstrumentProcessEngineModule::class])
interface DummyEngineComponent : EngineComponent
