@file:Suppress("BlockingMethodInNonBlockingContext")

package com.totec.trading.core.utils.dsljson

import com.dslplatform.json.DslJson
import com.dslplatform.json.JsonReader
import com.dslplatform.json.ParsingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.string.shouldMatch

class DSLJsonExtensionsTest : StringSpec() {
	
	init {
		val json = """{"success":true,"subscribe":"liquidation","request":{"op":"subscribe","args":["orderBookL2","orderBook10","trade","instrument","liquidation"]}}"""
		val timestampJson = """{"fundingTimestamp":"2020-07-03T20:00:00.000Z"}"""
		val dslJson = DslJson(DslJson.Settings<Any>().includeServiceLoader())
		
		"readTimestamp" {
			with(dslJson.newReader(timestampJson.toByteArray())) {
				startObject()
				nextToken // "
				readString() shouldMatch "fundingTimestamp"
				nextToken // :
				nextToken // "
				val timestamp = readTimestamp()
				timestamp shouldBeExactly 1593806400000L
			}
		}
		
		"fillObjectOrArray" {
			val reader = dslJson.newReader(json.toByteArray())
			fun JsonReader<Any>.toComma() {
				var token: Byte = nextToken
				while (token != ','.toByte()) {
					token = nextToken
				}
			}
			// get 2 commas
			reader.toComma()
			reader.toComma()
			reader.nextToken // start request
			reader.readString() shouldBeEqualComparingTo "request"
			reader.nextToken shouldBeEqualComparingTo ':'.toByte()
			reader.nextToken shouldBeEqualComparingTo '{'.toByte()
			reader.fillObjectOrArray('{')
			reader.nextToken shouldBeEqualComparingTo '}'.toByte()
			shouldThrow<ParsingException> {
				reader.nextToken
			}.message!! shouldBeEqualComparingTo "Unexpected end of JSON input"
		}
		
		"placeAfterField" {
			val reader = dslJson.newReader(json.toByteArray())
			reader.startObject()
			reader.nextToken
			reader.placeAfterField("subscribe")
			reader.nextToken // start liquidation
			reader.readString() shouldMatch "liquidation"
		}
		
		// this does the same as my placeAfterField, but is built in
		"startAttribute" {
			val reader = dslJson.newReader(json.toByteArray())
			reader.startObject()
			reader.startAttribute("subscribe")
			reader.nextToken // start liquidation
			reader.readString() shouldMatch "liquidation"
		}
	}
	
	class DoubleReader : ObjectReader<Double> {
		override fun JsonReader<*>.readObject(): Double {
			nextToken // start String
		readString() shouldMatch "number"
			nextToken // :
			nextToken // start Double
			val dbl = readDouble()
			endObject()
			return dbl
		}
	}
}