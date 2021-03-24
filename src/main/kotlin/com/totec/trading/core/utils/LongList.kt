/**
 * Created by ndrsh on 7/13/20
 */

package com.totec.trading.core.utils

class LongList {
	private var array: LongArray = LongArray(4) { Long.MIN_VALUE }
	var index = 0 // index of value we put in NEXT
	
	operator fun get(index: Int) = array[index]
	operator fun set(index: Int, value: Long) {
		if (index > this.array.size-1) throw IllegalArgumentException("Index $index too large because LongList's internal array only has size ${array.size}")
		array[index] = value
	}
	
	fun add(long: Long) {
		if (array.isLargeEnough() == false) {
			array.increaseSize()
		}
		array[index] = long
		index++
	}
	
	fun clearAll() {
		array.fill(element = Long.MIN_VALUE,
		           fromIndex = 0,
		           toIndex = index)
		index = 0
	}
	
	fun getAndRemoveLast(): Long {
		val last = last()
		removeLast()
		return last
	}
	
	fun removeLast() {
		if (isEmpty()) throw IllegalStateException("Cannot remove anything from LongList, because it is empty")
		array[index-1] = Long.MIN_VALUE
		index--
	}
	
	fun removeAllButLast() {
		if (index == 0) return
		if (index > 1) {
			array[0] = array[index-1]
			for (i in 1 until index) array[i] = Long.getInvalid()
			index = 1
		}
	}
	
	fun last() = array[index-1]
	
	fun isEmpty() = index == 0
	
	fun isNotEmpty() = isEmpty() == false
	
	fun getAverageAndRemoveAll(): Long {
		var sum = 0L
		var entries = index
		while (isNotEmpty()) sum += getAndRemoveLast()
		return sum / entries
	}
	
	private fun LongArray.isLargeEnough() = index <= size - 1
	
	private fun LongArray.increaseSize() {
		array = LongArray(size * 2) { i -> if (i < index) get(i) else Long.MIN_VALUE }
	}
}
