package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 9/6/15
 * Copyright(c) 2015 Level, Inc.
 */
public interface Listener<T: Any> {
    public fun onUpdate(data: T?)
}