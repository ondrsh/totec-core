/**
 * Created by ndrsh on 15.11.20
 */

package com.totec.trading.core.utils.recycling

/**
 * Track reference counts. Not implemented as Volatile or AtomicInteger because these objects should
 * generally not be shared across threads.
 */
interface Recyclable {
	var count: Int
	
	fun incCount() {
		count++
	}
	
	/**
	 * Call this publicly. If no instances are used anymore, it will be sent back into pool.
	 */
	fun decCount() {
		if (--count == 0) {
			backIntoPoolPrivate()
		}
	}
	
	/**
	 * Never call this directly.
	 */
	fun backIntoPoolPrivate()
}
