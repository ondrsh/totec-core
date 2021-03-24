/**
 * Created by ndrsh on 7/6/20
 */


package com.totec.trading.core.utils.dsljson

import com.dslplatform.json.JsonReader
import com.dslplatform.json.NumberConverter
import java.time.Instant

fun JsonReader<*>.toNextCommaOrClose(): Byte {
	fun Byte.isEnd() = when (this) {
		'}'.toByte(), ','.toByte(), ']'.toByte() -> true
		else                                     -> false
	}
	while (nextToken.isEnd() == false) { }
	return last()
}

fun JsonReader<*>.isNumberOrCompleteNull(): Boolean {
	nextToken // start number
	return if (last() == 'n'.toByte()) {
		readNull()
		false
	} else true
}

fun JsonReader<*>.readNull() {
	nextToken // u
	nextToken // l
	nextToken // l
}
fun JsonReader<*>.readDouble(): Double = NumberConverter.deserializeDouble(this)
fun JsonReader<*>.readDoubleNullable(): Double? = NumberConverter.NULLABLE_DOUBLE_READER.read(this)
fun JsonReader<*>.readLong(): Long = NumberConverter.deserializeLong(this)
fun JsonReader<*>.readLongNullable(): Long? = NumberConverter.NULLABLE_LONG_READER.read(this)

fun JsonReader<*>.readNullableTimestamp(): Long {
	return if (nextToken == '"'.toByte()) readTimestamp()
	else {
		nextToken // u
		nextToken // l
		nextToken // l
		Long.MIN_VALUE
	}
}

fun JsonReader<*>.readTimestamp(): Long {
	val s = readString()
	val instant = Instant.parse(s)
	return instant.toEpochMilli()
}

fun JsonReader<*>.fillObjectOrArray(startChar: Char) {
	var openObjects = 0
	var openArrays = 0
	fun Byte.process() {
		when (this) {
			'{'.toByte() -> openObjects++
			'}'.toByte() -> openObjects--
			'['.toByte() -> openArrays++
			']'.toByte() -> openArrays--
		}
	}
	startChar.toByte().process()
	while (openObjects != 0 || openArrays != 0) {
		nextToken.process()
	}
}

fun JsonReader<*>.placeAfterField(fieldName: String) {
	var field = readString()
	while (field != fieldName) {
		colon()
		when (nextToken) {
			'"'.toByte()               -> fillName()
			'['.toByte()               -> fillObjectOrArray('[')
			'{'.toByte()               -> fillObjectOrArray('{')
			't'.toByte(), 'f'.toByte() -> fillBoolean()
			else                       -> {
				if (nextToken.isNumber()) NumberConverter.deserializeNumber(this)
			}
		}
		// skip to next string
		if (nextToken != ','.toByte() || nextToken != '"'.toByte()) {
			throw RuntimeException("couldn't find field $fieldName")
		}
		field = readString()
	}
	semicolon()
	return
}

fun JsonReader<*>.fillBoolean() {
	if (last() == 't'.toByte()) {
		nextToken // r
		nextToken // u
		nextToken // e
	} else if (last() == 'f'.toByte()) {
		nextToken // a
		nextToken // l
		nextToken // s
		nextToken // e
	} else throw RuntimeException("couldn't fill boolean")
}

// also reads the first byte after the number
fun JsonReader<Nothing>.fillLong() {
	while (nextToken.isNumber()) {
		// loop
	}
}

fun Byte.isNumber(): Boolean {
	return (this == 0.toByte() ||
			this == 1.toByte() ||
			this == 2.toByte() ||
			this == 3.toByte() ||
			this == 4.toByte() ||
			this == 5.toByte() ||
			this == 6.toByte() ||
			this == 7.toByte() ||
			this == 8.toByte() ||
			this == 9.toByte() ||
			this == '.'.toByte())
}


fun JsonReader<*>.colon() {
	if (nextToken != 58.toByte()) {
		throw newParseError("Expecting ':'")
	}
}
