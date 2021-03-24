/**
 * Created by ndrsh on 5/19/20
 */

package com.totec.trading.core.instrument.book


/**
 * Things that can be ordered by sequence (for example certain BookEntry subclasses).
 *
 */
interface Sequence { val sequence: Long }
