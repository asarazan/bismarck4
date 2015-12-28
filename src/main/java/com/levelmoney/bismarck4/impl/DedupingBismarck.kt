package com.levelmoney.bismarck4.impl


/**
 * Created by Aaron Sarazan on 12/28/15.
 * Copyright(c) 2015 Level, Inc.
 *
 * This dedupes fetches by forcing sequential execution and checking timestamps.
 *
 * A fetch will be discarded iff [isFresh] returns true
 * and no [invalidate] occurred since the fetch was requested.
 */
class DedupingBismarck<T : Any> : BaseBismarck<T>() {

    private val lock = Any()

    override fun asyncFetch() {
        val ts = System.nanoTime()
        executor.execute {
            synchronized(lock) {
                val lastInvalidate = rateLimiter?.lastReset ?: 0L
                if (lastInvalidate > ts || !isFresh()) {
                    blockingFetch()
                }
            }
        }
    }
}