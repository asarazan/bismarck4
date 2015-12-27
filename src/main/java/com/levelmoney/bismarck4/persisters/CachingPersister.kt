package com.levelmoney.bismarck4.persisters

import com.levelmoney.bismarck4.Persister

/**
 * Created by Aaron Sarazan on 9/7/15
 * Copyright(c) 2015 Level, Inc.
 */
public open class CachingPersister<R: Any>(var cached: R? = null) : Persister<R> {
    override fun get(): R? = cached
    override fun put(data: R?) {
        cached = data
    }
}