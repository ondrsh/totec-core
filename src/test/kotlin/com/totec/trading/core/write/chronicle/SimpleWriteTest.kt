package com.totec.trading.core.write.chronicle

import com.totec.trading.core.instrument.book.BookEntry
import net.openhft.chronicle.bytes.Bytes
import net.openhft.chronicle.bytes.BytesMarshallable
import net.openhft.chronicle.bytes.ReadBytesMarshallable
import net.openhft.chronicle.queue.ChronicleQueue
import net.openhft.chronicle.queue.ExcerptAppender
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue
import net.openhft.chronicle.wire.MarshallableIn
import net.openhft.chronicle.wire.RawWire
import net.openhft.chronicle.wire.Wire
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import kotlin.random.Random


@Volatile
var counter = 0L
lateinit var queue: SingleChronicleQueue

fun main() {
	
	with(File("queue")) {
		if (exists()) deleteRecursively()
	}
	queue = ChronicleQueue.singleBuilder("queue").build()
	
	val nThreads = 5
	val pool = Executors.newFixedThreadPool(nThreads)
	repeat(nThreads) {
		pool.execute(Run())
	}
}

class Run : Thread() {
	override fun run() {
		val appender = queue.acquireAppender()
		val bookEntry = BookEntry.getNext(95023.28, 22220.51, System.currentTimeMillis())
		val iters = 20_000_000L
		val start = System.nanoTime()
		for (i in 1..iters) {
			bookEntry.price = Random.nextDouble()
			bookEntry.amount = Random.nextDouble()
			appender.writeBytes {
				it.writeDouble(bookEntry.price)
				it.writeDouble(bookEntry.amount)
			counter++
		}
		val end = System.nanoTime()
		val took = end - start
		println("took ${took / 1_000_000} millis")
		println("took ${1.0 * took / (iters)} nanos per write")
	}
}

fun ExcerptAppender.writeBytesFast(bytesMarshallable: BytesMarshallable) {
	val dc = writingDocument()
	bytesMarshallable.writeMarshallable(dc.wire()!!.bytes())
	dc.close()
}

fun MarshallableIn.readBytesFast(bytesReader: ReadBytesMarshallable): Boolean {
	val dc = readingDocument()
	if (dc.isPresent == false) return false
	bytesReader.readMarshallable(dc.wire()!!.bytes())
	dc.close()
	return true
}

val json = "{" +
		"\"table\":\"orderBookL2_25\"," +
		"\"action\":\"update\"," +
		"\"data\":[" +
		"{\"symbol\":\"XBTUSD\",\"id\":17999995000,\"side\":\"Buy\",\"size\":5}" +
		"]" +
		"}"
