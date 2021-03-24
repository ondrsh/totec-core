/**
 * Created by ndrsh on 02.12.20
 */

package com.totec.trading.core.engines

interface Engine

fun Iterable<Engine>.filterBookEngines() = filterIsInstance<BookEngine>()
