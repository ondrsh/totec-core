/**
 * Created by ndrsh on 5/18/20
 */

package com.totec.trading.core.instrument.book

/**
 * Message that holds content and gets passed through the system.
 *
 * @param T Type of the content
 * @property timestamp
 * @property content
 */
class Message<T> private constructor(var timestamp: Long, var content: T) {
	
/*	companion object : FastPool<Message<*>>() {
		override val initialArraySize: Int = 2048
		override var arr = Array(initialArraySize) { createEmpty() }
		
		override fun createEmpty() = Message(0L,
		                                     Unit) as Message<*>
		
		override fun createArray(newSize: Int) = Array(newSize) { i -> arrayInit(i)}
	}*/
}
