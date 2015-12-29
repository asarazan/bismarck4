package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.BaseBismarck


/**
 * Created by Aaron Sarazan on 12/26/15.
 * Copyright(c) 2015 Level, Inc.
 */

@Suppress("UNCHECKED_CAST")
fun <T : Any, B : BaseBismarck<T>> B.fetcher(fn: () -> T?): B {
    return fetcher(object: Fetcher<T> {
        override fun onFetch(): T? {
            return fn()
        }
    }) as B
}