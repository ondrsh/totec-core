/**
 * Created by ndrsh on 7/13/20
 */

package com.totec.trading.core.utils

class DoubleList {
	private var array: DoubleArray = DoubleArray(4) { Double.NaN }
	var index = 0
	
	operator fun get(index: Int) = array[index]
	operator fun set(index: Int, value: Double) {
		if (index > this.array.size-1) throw IllegalArgumentException("Index $index too large because DoubleList's internal array only has size ${array.size}")
		array[index] = value
	}
	
	fun add(dbl: Double) {
		if (array.isLargeEnough() == false) {
			array.increaseSize()
		}
		array[index] = dbl
		index++
	}
	
	fun clearAll() {
		array.fill(element = Double.NaN,
		           fromIndex = 0,
		           toIndex = index)
		index = 0
	}
	
	fun getAndRemoveLast(): Double {
		val last = last()
		removeLast()
		return last
	}
	
	fun removeLast() {
		if (isEmpty()) throw IllegalStateException("Cannot remove anything from DoubleList, because it is empty")
		array[index-1] = Double.NaN
		index--
	}
	
	fun removeAllButLast() {
		if (index == 0) return
		if (index > 1) {
			array[0] = array[index-1]
			for (i in 1 until index) array[i] = Double.getInvalid()
			index = 1
		}
	}
	
	fun getAverageAndRemoveAll(): Double {
		var sum = 0.0
		var entries = index
		while (isNotEmpty()) sum += getAndRemoveLast()
		return sum / entries
	}
	
	fun last() = array[index-1]
	
	fun isEmpty() = index == 0
	
	fun isNotEmpty() = isEmpty() == false
	
	private fun DoubleArray.isLargeEnough() = index <= size - 1
	
	private fun DoubleArray.increaseSize() {
		array = DoubleArray(size * 2) { i -> if (i < index) get(i) else Double.NaN }
	}
}
