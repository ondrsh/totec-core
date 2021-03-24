/**
 * Created by ndrsh on 6/2/20
 */

package com.totec.trading.core.instrument.book.ops

enum class OpType {
	INSERT { override val offset: Int = 200 },
	CHANGE { override val offset: Int = 300 },
	DELETE { override val offset: Int = 400 };
	
	abstract val offset: Int
}
