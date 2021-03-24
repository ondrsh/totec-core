/**
 * Created by ndrsh on 6/18/20
 */

package com.totec.trading.core.networking

import dagger.Component

@Component(modules = [DummyRestClientModule::class])
interface DummyRestClientComponent : RestClientComponent
