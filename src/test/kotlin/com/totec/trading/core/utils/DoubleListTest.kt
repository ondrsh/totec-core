package com.totec.trading.core.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import kotlin.IllegalStateException
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class DoubleListTest : StringSpec() {
	
	init {
		val dblList = spyk(DoubleList())
		
		"add" {
			dblList.isInvalid()
			dblList.getArray().size shouldBeExactly 4
			dblList.add(3.6)
			dblList.getArray().size shouldBeExactly 4
			dblList.add(5.4)
			dblList.getArray().size shouldBeExactly 4
			dblList.add(1.5)
			dblList.getArray().size shouldBeExactly 4
			dblList.add(1.0)
			dblList.getArray().size shouldBeExactly 4
			dblList.add(14.1)
			dblList.getArray().size shouldBeExactly 8
		}
		
		"get(),set()" {
			dblList[0] shouldBeExactly 3.6
			dblList[1] shouldBeExactly 5.4
			dblList[2] shouldBeExactly 1.5
			dblList[3] shouldBeExactly 1.0
			dblList[4] shouldBeExactly 14.1
			dblList[1] = 0.01
			dblList[1] shouldBeExactly 0.01
			shouldThrow<ArrayIndexOutOfBoundsException> {
				dblList[8]
			}
			shouldThrow<IllegalArgumentException> {
				dblList[8] = 1.121
				null
			}
		}
		
		"clearAll()" {
			dblList.index shouldBeExactly 5
			dblList.clearAll()
			dblList.index shouldBeExactly 0
			dblList.isInvalid()
		}
		
		"last(),removeLast(),getAndRemoveLast(),isEmpty()" {
			val list = DoubleList()
			list.isEmpty() shouldBe true
			shouldThrow<IllegalStateException> { list.removeLast() }
			list.add(1.2)
			list.isEmpty() shouldBe false
			list.add(2.0)
			list.last() shouldBeExactly 2.0
			list.removeLast()
			list.last() shouldBeExactly 1.2
			list.index shouldBeExactly 1
			list.isEmpty() shouldBe false
			list.add(5.4)
			list.getAndRemoveLast() shouldBeExactly 5.4
			list.getAndRemoveLast() shouldBeExactly 1.2
			shouldThrow<ArrayIndexOutOfBoundsException> {  list.getAndRemoveLast() }
			list.isEmpty() shouldBe true
		}
		
		"removeAllButLast()" {
			val list = DoubleList()
			list.add(1.2)
			list.add(12.0)
			list.add(7.1)
			list.add(1.7)
			list.add(20.1)
			list.removeAllButLast()
			list.index shouldBeExactly 1
			list.last() shouldBeExactly 20.1
		}
		
		"getAverageAndRemoveAll" {
			val list = DoubleList()
			list.add(1.0)
			list.add(13.0)
			list.add(70.0)
			list.add(112.0)
			list.add(20.0)
			list.index shouldBeExactly 5
			list.getAverageAndRemoveAll() shouldBeExactly 43.2
			list.index shouldBeExactly 0
			list.isEmpty() shouldBe true
			for (i in 0..4) list[i] shouldBe Double.NaN
		}
	}
	
}

fun DoubleList.getArray(): DoubleArray {
	val arrayField = DoubleList::class.members.find { it.name == "array" }!! as KMutableProperty<*>
	arrayField.isAccessible = true
	return arrayField.javaField!!.get(this) as DoubleArray
}

fun DoubleList.isInvalid() {
	val arr = getArray()
	for (d in arr) {
		d.isNaN() shouldBe true
	}
}