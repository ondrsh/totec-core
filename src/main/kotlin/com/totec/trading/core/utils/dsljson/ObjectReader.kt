/**
 * Created by ndrsh on 7/12/20
 */

package com.totec.trading.core.utils.dsljson

import com.dslplatform.json.JsonReader

interface ObjectReader<T> {
	/**
	 * You don't have to start the object with [JsonReader.startObject], but you have to close it with [JsonReader.endObject]
	 */
	fun JsonReader<*>.readObject(): T?
	
	fun JsonReader<*>.readSet(): MutableSet<T>? = mutableSetOf<T>().also { if (addAllToCollection(it)) return it else return@readSet null }
	fun JsonReader<*>.readList(): MutableList<T>? = mutableListOf<T>().also { if (addAllToCollection(it)) return it else return@readList null }
	
	/**
	 * Returns true if something was added to the collection and false otherwise
	 */
	fun JsonReader<*>.addAllToCollection(collection: MutableCollection<T>): Boolean {
		if (last() != '['.toByte()) throw newParseError("Expecting '[' as set start")
		var somethingAdded = false
		if (nextToken == ']'.toByte()) return somethingAdded // if it is not ']', it has to be '{'
		val first = readObject()
		if (first != null) {
			collection.add(first)
			somethingAdded = true
		}
		while (nextToken == ','.toByte()) { // if it's not ',', it is assumed to be ']'
			startObject()
			val another = readObject()
			if (another != null) {
				collection.add(another)
				somethingAdded = true
			}
		}
		checkArrayEnd()
		return somethingAdded
	}
}
