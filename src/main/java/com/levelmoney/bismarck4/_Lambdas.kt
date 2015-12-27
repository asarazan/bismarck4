package com.levelmoney.bismarck4


/**
 * Created by Aaron Sarazan on 12/26/15.
 * Copyright(c) 2015 Level, Inc.
 */

fun <T : Any> Bismarck<T>.listen(fn: (T?) -> Unit): Bismarck<T> {
    return listen(object: Listener<T> {
        override fun onUpdate(data: T?) {
            fn(data)
        }
    }, Bismarck.POSITION_END)
}

fun <T : Any> Bismarck<T>.listenFront(fn: (T?) -> Unit): Bismarck<T> {
    return listen(object: Listener<T> {
        override fun onUpdate(data: T?) {
            fn(data)
        }
    }, Bismarck.POSITION_BEGIN)
}