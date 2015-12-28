package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 9/7/15
 * Copyright(c) 2015 Level, Inc.
 */
public interface Fetcher<T: Any> {
    public fun onFetch(): T?
}