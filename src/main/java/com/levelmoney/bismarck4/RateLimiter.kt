package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 4/2/14
 * Copyright(c) 2014 Level, Inc.
 */
public interface RateLimiter {

    val lastReset: Long

    fun update()
    fun reset()
    fun isFresh(): Boolean
}
