package com.totec.trading.core.collections

import com.totec.trading.core.utils.arraytreeset.ArrayTreeSet
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ArrayTreeSetTest : StringSpec(
	{

		val map = ArrayTreeSet<Int>()
		
		"map adding" {
			map.add(10)
			map.add(7)
			map.add(2)
			map.add(15)
			
			map[0] shouldBe 2
			map[1] shouldBe 7
			map.last() shouldBe 15
			map.entryIndex(10) shouldBe 2
			map.size shouldBe 4
			
			map.add(-2)
			map.add(4)
			map.add(0)
			
			map[0] shouldBe -2
			map[1] shouldBe 0
			map[2] shouldBe 2
			map[6] shouldBe 15
			map[7] shouldBe null
			map.last() shouldBe 15
			map.entryIndex(10) shouldBe 5
			map.size shouldBe 7
		}
		
		"removing" {
			map.remove(0)
			map.remove(4)
			
			map[1] shouldBe 2
			map[2] shouldBe 7
			map[4] shouldBe 15
			map.size shouldBe 5
			map[6] shouldBe null
		}
		
		"using addAll" {
			map.addAll(arrayOf(1, -6, -3))
			map[1] shouldBe -3
			map.exact(1) shouldBe -3
			map.entryIndex(-3) shouldBe 1
			
			map[2] shouldBe -2
			map.exact(2) shouldBe -2
			map.entryIndex(-2) shouldBe 2
			map.size shouldBe 8
			map[8] shouldBe null
			map[7] shouldNotBe null
		}
	})
