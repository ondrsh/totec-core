/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.utils.recycling

import com.totec.trading.core.instrument.book.BookEntry


/**
 * Objects that want to be pooled should have a companion extend this. Don't forget to **1)** make the main constructor
 * private and **2)** to provide a custom `create` function in the final companion class
 * with specific parameters in the final companion class for automated copying - see [BookEntry] class for an example.
 */
abstract class FastPool<T: Recyclable> {
	abstract var arr: Array<T>
	abstract val initialArraySize: Int
	protected var index = 0
	
	/**
	 * Returns the next free (probably already used before) object in the pool. If possible, write and use a custom `create` function
	 * that also initializes the object properly (overwriting all the fields of the old object).
	 */
	fun getNext(): T {
		if (index >= arr.size - 1) {
			increaseArr()
		}
		return arr[index++]
	}
	
	/**
	 * Doubles the size of the Array.
	 *
	 */
	private fun increaseArr() {
		val larger = createArray(arr.size * 2)
		arr = larger
	}
	
	/**
	 * Recycles the [instance] and puts it back into the pool. **Do not forget to recycle all objects that fall out of scope**
	 * otherwise you will have memory leaks and the pool will grow indefinitely.
	 */
	fun backIntoPool(instance: T) {
		if (index > 0) arr[--index] = instance
	}
	
	/**
	 * Provide objects when creating an array. If array size gets increased, we will put old objects in the new array at the beginning.
	 *
	 * @param i Index when creating the new array.
	 */
	protected fun arrayInit(i: Int) = if (i < arr.size) arr[i] else createEmpty()
	
	/**
	 * Provide a default function for creating an object. It's preferable to set all primitives to values that do not make sense
	 * so testing catches them (e.g. negative price).
	 *
	 * @return A default object that gets created at initialization of the companion class.
	 */
	abstract fun createEmpty(): T
	
	/**
	 * Provide a default array for the given class. Unfortunately this is needed because when creating an array, the JVM needs to
	 * know the parameter type at compile time.
	 */
	protected abstract fun createArray(newSize: Int): Array<T>
	
	private fun resetPoolIndex() {
		index = 0
	}
	
	private fun resetPoolArray() {
		arr = createArray(initialArraySize)
	}
	
	fun resetPool() {
		resetPoolArray()
		resetPoolIndex()
	}
}
