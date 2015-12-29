package com.levelmoney.bismarck4.ratelimit

import com.levelmoney.bismarck4.RateLimiter
import java.util.concurrent.TimeUnit

/**
 * Created by Aaron Sarazan on 9/10/13
 * Copyright(c) 2013 Level, Inc.
 */
public class SimpleRateLimiter(val ms: Long) : RateLimiter {

    /**
     * Uses [System.nanoTime]
     */
    public var lastRun: Long = 0
        private set

    /**
     * Uses [System.nanoTime]
     */
    override var lastReset: Long = 0
        private set

    override fun update() {
        lastRun = getCurrent()
    }

    override fun reset() {
        lastRun = 0
        lastReset = getCurrent()
    }

    override fun isFresh(): Boolean {
        return !pass(getCurrent())
    }

    private fun getCurrent(): Long {
        return System.nanoTime()
    }

    private fun pass(current: Long): Boolean {
        val last = lastRun
        val ns = TimeUnit.MILLISECONDS.toNanos(ms)
        return last == 0L || current - last >= ns
    }
}
