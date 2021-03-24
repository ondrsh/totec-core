/**
 * Created by ndrsh on 20.10.20
 */

package com.totec.trading.core.instrument

import com.totec.trading.core.utils.LongList
import com.totec.trading.core.utils.getInvalid

class Lag {
	val lagList = LongList()
	var writtenTimestamp = Long.getInvalid()
}
