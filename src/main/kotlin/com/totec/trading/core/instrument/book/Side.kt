/**
 * Created by ndrsh on 5/28/20
 */

package com.totec.trading.core.instrument.book

enum class Side {
	Buy {
		override val offset: Int = 1
	},
	Sell {
		override val offset: Int = 17
	};
	
	abstract val offset: Int
}
