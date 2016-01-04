package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 1/4/16.
 * Copyright(c) 2016 Level, Inc.
 */
interface Listener<T : Any> {
    public fun onUpdate(data: T?)
}