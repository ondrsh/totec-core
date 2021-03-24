/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.write.chronicle

import com.totec.trading.core.instrument.book.BookEntry
import net.openhft.chronicle.queue.ChronicleQueue
import java.io.File


fun main() {
	
	with(File("queue/test/BookEntry")) {
		if (exists()) deleteRecursively()
	}
	queue = ChronicleQueue.singleBuilder("queue/test/BookEntry").build()
	
}