/**
 * Created by ndrsh on 31.10.20
 */

package com.totec.trading.core.write.chronicle

import net.openhft.chronicle.bytes.BytesOut
import net.openhft.chronicle.queue.ExcerptAppender

inline fun ExcerptAppender.writeBytesRaw(crossinline block: BytesOut<*>.() -> Unit) {
	writeBytes { it.block() }
}
