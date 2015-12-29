package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 9/7/15
 * Copyright(c) 2015 Level, Inc.
 */
public interface Fetcher<T: Any> {

    @Throws(BismarckFetchError::class)
    public fun onFetch(): T?

    class BismarckFetchError(message: String, cause: Throwable? = null) : Throwable(message, cause)
}