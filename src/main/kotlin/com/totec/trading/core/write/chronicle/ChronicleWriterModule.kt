/**
 * Created by ndrsh on 8/4/20
 */

package com.totec.trading.core.write.chronicle

import com.totec.trading.core.engines.Engine
import dagger.Module
import dagger.Provides

@Module
class ChronicleWriterModule {
	@Provides fun provideChronicleWriter(chronicleWriter: ChronicleWriter): Engine = chronicleWriter
}
