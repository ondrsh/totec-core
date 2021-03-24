/**
 * Created by ndrsh on 5/14/20
 */

package com.totec.trading.core.utils.io

import java.io.File

/**
 *
 * Extension for creating a directory from a [String].
 * Non-existing parent directories will be created automatically.
 *
 * @returns nothing
 */
fun String.createDir() = File(this).mkdirs()
