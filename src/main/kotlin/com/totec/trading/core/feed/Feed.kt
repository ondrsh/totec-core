/**
 * Created by ndrsh on 6/8/20
 */

package com.totec.trading.core.feed

import com.totec.trading.core.partition.Partition

interface Feed {
	val partition: Partition
}
