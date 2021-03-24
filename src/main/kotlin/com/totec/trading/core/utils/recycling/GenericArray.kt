/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.utils.recycling


/**
 * Normal array that can be created without knowing the type at runtime.
 * Call it via [createArray].
 *
 * @param T
 * @property size
 * @property arr
 */
class GenericArray<T>
@PublishedApi internal constructor(private var size: Int, private var arr: Array<T>) {
	var index = 0
	
	operator fun get(index: Int): T {
		return arr[index]
	}
	
	operator fun set(index: Int, obj: T) {
		arr[index] = obj
	}
	
}

/**
 * Create an array with type [T].
 *
 * @param T
 * @param size
 * @param init
 */
inline fun <reified T> createArray(size: Int, init: () -> T) = GenericArray(size, Array(size) { init() })
