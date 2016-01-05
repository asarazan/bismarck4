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

@Suppress("UNCHECKED_CAST")
fun <T : Any, B : Bismarck<T>> B.listen(fn: (T?) -> Unit): B {
    return listen(object : Listener<T> {
        override fun onUpdate(data: T?) {
            fn(data)
        }
    }) as B
}

/**
 * Performs the given [action] on each element.
 *
 * We're still getting crashes related to https://youtrack.jetbrains.com/issue/KT-10479
 * on Android clients, so this should work around it for the time being.
 */
public inline fun <T> Iterable<T>.forEachCompat(action: (T) -> Unit): Unit {
    for (element in this) action(element)
}