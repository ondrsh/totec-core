/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.utils

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Gives the runtime property value given [instance] and [propertyName]
 */
@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.memberProperties
                     // don't cast here to <Any, R>, it would succeed silently
                     .first { it.name == propertyName } as KProperty1<Any, *>
    property.isAccessible = true
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}
