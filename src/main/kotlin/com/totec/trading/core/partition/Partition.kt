/**
 * Created by ndrsh on 6/7/20
 */

package com.totec.trading.core.partition

import com.totec.trading.core.feed.Feed

interface Partition {
	val feed: Feed
}
