package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 9/25/14
 * Copyright(c) 2014 Level, Inc.
 */
public interface Persister<T: Any> {
    public fun get(): T?
    public fun put(data: T?)
}
