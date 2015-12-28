package com.levelmoney.bismarck4.persisters

import com.levelmoney.bismarck4.Persister

/**
 * Created by Aaron Sarazan on 9/7/15
 * Copyright(c) 2015 Level, Inc.
 */
public open class CachingPersister<T: Any>(var cached: T? = null) : Persister<T> {
    override fun get(): T? = cached
    override fun put(data: T?) {
        cached = data
    }
}