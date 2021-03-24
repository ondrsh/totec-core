/**
 * Created by ndrsh on 5/30/20
 */

package com.totec.trading.core.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import java.lang.Double.doubleToLongBits
import kotlin.random.Random


class SnippetTest : StringSpec (
	{
		
		"double to long Bits" {
			repeat(1000) {
				val d = Random.nextDouble()
				val bits = doubleToLongBits(d)
				(bits xor (bits ushr 32)).toInt() shouldBeExactly d.hashCode()
			}
		}
		
		"MutableList<String>.removeAllButLast()" {
			val stringList = mutableListOf("peter", "fritz", "alessandro")
			stringList.removeAllButLast()
			stringList.last() shouldMatch "alessandro"
			stringList.size shouldBeExactly 1
		}
})
