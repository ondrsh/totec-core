package com.totec.trading.core.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class LongListTest : StringSpec() {
	
	init {
		val longList = spyk(LongList())
		
		"add" {
			longList.isInvalid()
			longList.getArray().size shouldBeExactly 4
			longList.add(3)
			longList.getArray().size shouldBeExactly 4
			longList.add(5)
			longList.getArray().size shouldBeExactly 4
			longList.add(1)
			longList.getArray().size shouldBeExactly 4
			longList.add(10)
			longList.getArray().size shouldBeExactly 4
			longList.add(14)
			longList.getArray().size shouldBeExactly 8
		}
		
		"get(),set()" {
			longList[0] shouldBeExactly 3
			longList[1] shouldBeExactly 5
			longList[2] shouldBeExactly 1
			longList[3] shouldBeExactly 10
			longList[4] shouldBeExactly 14
			longList[1] = 250
			longList[1] shouldBeExactly 250
			shouldThrow<ArrayIndexOutOfBoundsException> {
				longList[8]
			}
			shouldThrow<IllegalArgumentException> {
				longList[8] = 5200
				null
			}
		}
		
		"clearAll()" {
			longList.index shouldBeExactly 5
			longList.clearAll()
			longList.index shouldBeExactly 0
			longList.isInvalid()
		}
		
		"last(),removeLast(),getAndRemoveLast(),isEmpty()" {
			val list = LongList()
			list.isEmpty() shouldBe true
			shouldThrow<IllegalStateException> { list.removeLast() }
			list.add(20_000_000_000)
			list.isEmpty() shouldBe false
			list.add(25)
			list.last() shouldBeExactly 25
			list.removeLast()
			list.last() shouldBeExactly 20_000_000_000
			list.index shouldBeExactly 1
			list.isEmpty() shouldBe false
			list.add(5)
			list.getAndRemoveLast() shouldBeExactly 5
			list.getAndRemoveLast() shouldBeExactly 20_000_000_000
			shouldThrow<ArrayIndexOutOfBoundsException> {  list.getAndRemoveLast() }
			list.isEmpty() shouldBe true
		}
		
		"removeAllButLast()" {
			val list = LongList()
			list.add(1)
			list.add(12)
			list.add(7)
			list.add(1)
			list.add(20)
			list.removeAllButLast()
			list.index shouldBeExactly 1
			list.last() shouldBeExactly 20
		}
		
		"getAverageAndRemoveAll" {
			val list = LongList()
			list.add(1)
			list.add(13)
			list.add(70)
			list.add(112)
			list.add(20)
			list.index shouldBeExactly 5
			list.getAverageAndRemoveAll() shouldBeExactly 43 // 43.2, but rounding down
			list.index shouldBeExactly 0
			list.isEmpty() shouldBe true
			for (i in 0..4) list[i] shouldBeExactly Long.getInvalid()
		}
	}
	
}

fun LongList.getArray(): LongArray {
	val arrayField = LongList::class.members.find { it.name == "array" }!! as KMutableProperty<*>
	arrayField.isAccessible = true
	return arrayField.javaField!!.get(this) as LongArray
}

fun LongList.isInvalid() {
	val arr = getArray()
	for (l in arr) {
		l shouldBeExactly Long.MIN_VALUE
	}
}
