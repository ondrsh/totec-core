package com.totec.trading.core.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.string.shouldStartWith
import kotlin.properties.Delegates

class RoundTest : StringSpec(
	{

		"round price" {
			(0.1 + 0.2).roundPrice() shouldBeExactly 0.3
		}
		
		"round price big inaccurate" {
			(100_000_000.1 + 200_000_000.2).roundPrice() shouldBeExactly 3.0000000029999995E8
		}
		
		"round price bigger fail" {
			val ex = shouldThrow<IllegalStateException> {
				(1_000_000_000.1 + 2_000_000_000.2).roundPrice()
			}
			ex.message shouldStartWith "Tried to round"
		}
		
		"round price bigger safe" {
			(1_000_000_000.1 + 2_000_000_000.2).roundPriceSafe() shouldBeExactly 3_000_000_000.3
		}
		
		"round amount" {
			(0.1 + 0.2).roundAmount() shouldBeExactly 0.3
		}
		
		"round amount big inaccurate" {
			(100_000_000.1 + 200_000_000.2).roundAmount() shouldBeExactly 3.0000000029999995E8
		}
		
		"round amount biggest amount possible" {
			(1_000_000_000.1 + 2_000_000_000.2).roundAmount() shouldBeExactly 3_000_000_000.3
		}
		
		"round amount bigger fail" {
			val ex = shouldThrow<IllegalStateException> {
				(10_000_000_000.1 + 20_000_000_000.2).roundAmount()
			}
			ex.message shouldStartWith "Tried to round"
		}
		
		"round amount bigger safe" {
			(10_000_000_000.1 + 20_000_000_000.2).roundAmountSafe() shouldBeExactly 30_000_000_000.300003
		}
	}
)
