package com.levelmoney.bismarck4.impl

import com.levelmoney.bismarck4.Bismarck


/**
 * Created by Aaron Sarazan on 12/27/15.
 * Copyright(c) 2015 Level, Inc.
 */
object BismarckFetching {

    private val fetches = linkedListOf<()->Unit>()
    private val thread = Thread {
        while (true) {
            synchronized(fetches) {
                fetches.pollFirst()?.invoke()
            }
        }
    }.apply { start() }

    fun <T : Any> add(b: Bismarck<T>, onComplete: (() -> Unit)? = null) {
        synchronized(fetches) {
            fetches.add {
                b.blockingFetch()
                onComplete?.invoke()
            }
        }
    }
}