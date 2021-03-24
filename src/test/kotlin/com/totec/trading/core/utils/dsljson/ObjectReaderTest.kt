/**
 * Created by ndrsh on 7/12/20
 */

@file:Suppress("BlockingMethodInNonBlockingContext")

package com.totec.trading.core.utils.dsljson

import com.dslplatform.json.DslJson
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.beInstanceOf
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should

class ObjectReaderTest : StringSpec() {
	
	init {
		val jsonArray = """[{"number":1.32322},{"number":5.22525},{"number":66333}]"""
		val dslJson = DslJson(DslJson.Settings<Any>().includeServiceLoader())
		
		"readSet" {
			val reader = dslJson.newReader(jsonArray.toByteArray())
			reader.startArray()
			val readSet = with(DSLJsonExtensionsTest.DoubleReader()) {
				reader.readSet()
			}
			val correctSet = setOf(1.32322, 5.22525, 66333.0)
			readSet shouldContainExactly correctSet
			readSet should beInstanceOf(Set::class)
		}
		
		"readList" {
			val reader = dslJson.newReader(jsonArray.toByteArray())
			reader.startArray()
			val readList = with(DSLJsonExtensionsTest.DoubleReader()) {
				reader.readList()
			}
			val correctList = listOf(1.32322, 5.22525, 66333.0)
			readList shouldContainExactly correctList
			readList should beInstanceOf(List::class)
		}
	}
}