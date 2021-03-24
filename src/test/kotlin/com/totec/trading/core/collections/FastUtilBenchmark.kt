/**
 * Created by ndrsh on 5/22/20
 */

package com.totec.trading.core.collections

import com.totec.trading.core.instrument.book.BookEntry
import com.totec.trading.core.benchmark.bench
import io.timeandspace.smoothie.OptimizationObjective
import io.timeandspace.smoothie.SmoothieMap
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.random.Random


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Threads(0)
open class FastUtilBenchmark {
	
	@State(Scope.Benchmark)
	open class MapState {
		val normalHashMap = HashMap<Double, BookEntry>()
		val smoothieMap2 = SmoothieMap.newBuilder<BookEntry, BookEntry>()
			.optimizeFor(OptimizationObjective.LOW_GARBAGE)
			.build()
		val smoothieMap1 = net.openhft.smoothie.SmoothieMap<BookEntry, BookEntry>()
		val bidsComparator = Comparator<BookEntry> { b1, b2 -> b1.price.compareTo(b2.price) }
		val fastUtilAVLTreeSet = ObjectAVLTreeSet(bidsComparator)
		val fastUtilRBTreeSet = ObjectRBTreeSet(bidsComparator)
		val normalTreeSet = TreeSet(bidsComparator)
		
		
		private val allMaps: List<MutableMap<Double, BookEntry>> = listOf(normalHashMap)
		
		init {
			repeat(1998) {
				val entry = BookEntry.getNext(Random.nextDouble(6500.0, 9800.0), Random.nextDouble(1.0, 100_000.0), System.currentTimeMillis())
				allMaps.forEach { it[entry.price] = entry }
				fastUtilAVLTreeSet.add(entry)
				fastUtilRBTreeSet.add(entry)
				normalTreeSet.add(entry)
				smoothieMap2[entry] = entry
				smoothieMap1[entry] = entry
			}
			EntryState().allEntries.forEach { entry ->
				allMaps.forEach { it[entry.price] = entry }
				fastUtilAVLTreeSet.add(entry)
				fastUtilRBTreeSet.add(entry)
				normalTreeSet.add(entry)
				smoothieMap2[entry] = entry
				smoothieMap1[entry] = entry
			}
		}
	}
	
	@State(Scope.Benchmark)
	open class EntryState {
		var entry1 = BookEntry.getNext(6539.0, 1200.0, System.currentTimeMillis())
		var entry1Consume = BookEntry.getNext(9799.0, 1200.0, System.currentTimeMillis())
		var entry2 = BookEntry.getNext(6501.5, 100.0, System.currentTimeMillis())
		var entry2Consume = BookEntry.getNext(9789.0, 100.0, System.currentTimeMillis())
		var entry3 = BookEntry.getNext(6499.5, 200.0, System.currentTimeMillis())
		var entry3Consume = BookEntry.getNext(9099.2, 200.0, System.currentTimeMillis())
		var entry4 = BookEntry.getNext(6502.0, 700.0, System.currentTimeMillis())
		var entry4Consume = BookEntry.getNext(9712.0, 700.0, System.currentTimeMillis())
		var entry5 = BookEntry.getNext(6506.0, 80.0, System.currentTimeMillis())
		var entry5Consume = BookEntry.getNext(9801.0, 80.0, System.currentTimeMillis())
		
		val entryArr = Array<BookEntry?>(10) { null }
		var removal = true
		val allEntries = listOf(entry1, entry2, entry3, entry4, entry5)
	}
	
	@Benchmark fun normalHashMapGet(myState: MapState, entryState: EntryState): BookEntry {
		return myState.normalHashMap[entryState.entry1.price]!!
	}
	
	@Benchmark fun fastSmoothieMap2Get(myState: MapState, entryState: EntryState): BookEntry {
		return myState.smoothieMap2[entryState.entry1]!!
	}
	
	@Benchmark fun fastSmoothieMap1Get(myState: MapState, entryState: EntryState): BookEntry {
		return myState.smoothieMap1[entryState.entry1]!!
	}
	
	@Benchmark fun hashMapChange(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		entryState.entry1Consume = mapState.normalHashMap.remove(entryState.entry1.price)!!
		mapState.normalHashMap[entryState.entry1.price] = entryState.entry1
		entryState.entry2Consume = mapState.normalHashMap.remove(entryState.entry2.price)!!
		mapState.normalHashMap[entryState.entry2.price] = entryState.entry2
		entryState.entry3Consume = mapState.normalHashMap.remove(entryState.entry3.price)!!
		mapState.normalHashMap[entryState.entry3.price] = entryState.entry3
		entryState.entry4Consume = mapState.normalHashMap.remove(entryState.entry4.price)!!
		mapState.normalHashMap[entryState.entry4.price] = entryState.entry4
		entryState.entry5Consume = mapState.normalHashMap.remove(entryState.entry5.price)!!
		mapState.normalHashMap[entryState.entry5.price] = entryState.entry5
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	@Benchmark fun smoothieMapChange(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		entryState.entry1Consume = mapState.smoothieMap2.remove(entryState.entry1)!!
		mapState.smoothieMap2[entryState.entry1] = entryState.entry1
		entryState.entry2Consume = mapState.smoothieMap2.remove(entryState.entry2)!!
		mapState.smoothieMap2[entryState.entry2] = entryState.entry2
		entryState.entry3Consume = mapState.smoothieMap2.remove(entryState.entry3)!!
		mapState.smoothieMap2[entryState.entry3] = entryState.entry3
		entryState.entry4Consume = mapState.smoothieMap2.remove(entryState.entry4)!!
		mapState.smoothieMap2[entryState.entry4] = entryState.entry4
		entryState.entry5Consume = mapState.smoothieMap2.remove(entryState.entry5)!!
		mapState.smoothieMap2[entryState.entry5] = entryState.entry5
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	@Benchmark fun fastUtilAVLTreeSetChange(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		mapState.fastUtilAVLTreeSet.remove(entryState.entry1)
		mapState.fastUtilAVLTreeSet.add(entryState.entry1)
		mapState.fastUtilAVLTreeSet.remove(entryState.entry2)
		mapState.fastUtilAVLTreeSet.add(entryState.entry2)
		mapState.fastUtilAVLTreeSet.remove(entryState.entry3)
		mapState.fastUtilAVLTreeSet.add(entryState.entry3)
		mapState.fastUtilAVLTreeSet.remove(entryState.entry4)
		mapState.fastUtilAVLTreeSet.add(entryState.entry4)
		mapState.fastUtilAVLTreeSet.remove(entryState.entry5)
		mapState.fastUtilAVLTreeSet.add(entryState.entry5)
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	// @OperationsPerInvocation(5)
	@Benchmark fun fastUtilAVLTreeSetIterate(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		val iter = mapState.fastUtilAVLTreeSet.iterator()
		for (i in 0 until 10) {
			entryState.entryArr[i] = iter.next()
		}
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	// @OperationsPerInvocation(5)
	@Benchmark fun fastUtilRBTreeSetChange(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		mapState.fastUtilRBTreeSet.remove(entryState.entry1)
		mapState.fastUtilRBTreeSet.add(entryState.entry1)
		mapState.fastUtilRBTreeSet.remove(entryState.entry2)
		mapState.fastUtilRBTreeSet.add(entryState.entry2)
		mapState.fastUtilRBTreeSet.remove(entryState.entry3)
		mapState.fastUtilRBTreeSet.add(entryState.entry3)
		mapState.fastUtilRBTreeSet.remove(entryState.entry4)
		mapState.fastUtilRBTreeSet.add(entryState.entry4)
		mapState.fastUtilRBTreeSet.remove(entryState.entry5)
		mapState.fastUtilRBTreeSet.add(entryState.entry5)
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	// @OperationsPerInvocation(5)
	@Benchmark fun fastUtilRBTreeSetIterate(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		val iter = mapState.fastUtilRBTreeSet.iterator()
		for (i in 0 until 10) {
			entryState.entryArr[i] = iter.next()
		}
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	// @OperationsPerInvocation(5)
	@Benchmark fun normalTreeSetChange(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		mapState.normalTreeSet.remove(entryState.entry1)
		mapState.normalTreeSet.add(entryState.entry1)
		mapState.normalTreeSet.remove(entryState.entry2)
		mapState.normalTreeSet.add(entryState.entry2)
		mapState.normalTreeSet.remove(entryState.entry3)
		mapState.normalTreeSet.add(entryState.entry3)
		mapState.normalTreeSet.remove(entryState.entry4)
		mapState.normalTreeSet.add(entryState.entry4)
		mapState.normalTreeSet.remove(entryState.entry5)
		mapState.normalTreeSet.add(entryState.entry5)
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
	
	// @OperationsPerInvocation(5)
	@Benchmark fun normalTreeSetIterate(mapState: MapState, entryState: EntryState, blackhole: Blackhole) {
		val iter = mapState.normalTreeSet.iterator()
		for (i in 0 until 10) {
			entryState.entryArr[i] = iter.next()
		}
		blackhole.consume(mapState)
		blackhole.consume(entryState)
	}
}

fun main() = FastUtilBenchmark::class.bench()
