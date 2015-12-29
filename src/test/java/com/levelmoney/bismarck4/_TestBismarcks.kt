package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.BaseBismarck


/**
 * Created by Aaron Sarazan on 12/28/15.
 * Copyright(c) 2015 Level, Inc.
 */

fun sleepAsyncFetch(ms: Long = 5L) = Thread.sleep(ms)

fun BaseBismarck<*>._asyncFetch() {
    val method = BaseBismarck::class.java.getDeclaredMethod("asyncFetch")
    method.isAccessible = true
    method.invoke(this)
}