package com.totec.trading.core.utils.arraytreeset

class ArrayTreeSet<T>: IndexedTreeSet<T>() {
	operator fun get(index: Int): T? = exact(index)
	
	// fun getFirst(num: Int) = Array<T>(num) { i -> this[i]!!}
	
}
class DoubleArrayTreeSet: IndexedTreeSet<Double>() {
	operator fun get(index: Int): Double? = exact(index)
	fun getFirst(num: Int) = List(num) { i -> exact(i)}
}

class LongArrayTreeSet: IndexedTreeSet<Long>() {
	operator fun get(index: Int): Long? = exact(index)
	fun getFirst(num: Int) = List(num) { i -> exact(i)}
}

class StringArrayTreeSet: IndexedTreeSet<String>() {
	operator fun get(index: Int): String? = exact(index)
	fun getFirst(num: Int) = List(num) { i -> exact(i)}
}
