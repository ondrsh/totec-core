/**
 * Created by ndrsh on 6/18/20
 */

package com.totec.trading.core.networking

import dagger.Binds
import dagger.Module

@Module
interface DummyRestClientModule {
	@Binds fun restClient(dummyRestClient: DummyRestClient): RestClient
}
