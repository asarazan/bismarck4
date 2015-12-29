package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.DedupingBismarck
import org.junit.Test
import rx.Subscription
import kotlin.test.assertEquals

/**
 * Created by Aaron Sarazan on 12/28/15.
 * Copyright(c) 2015 Level, Inc.
 */
class DedupingTests {

    @Test
    fun testBasicDedupe() {
        var count = 0
        val b = DedupingBismarck<Int>().fetcher { count++ }
        b._asyncFetch()
        b._asyncFetch() // should be dropped
        sleepAsyncFetch()
        assertEquals(1, count)
        b.invalidate()
        b._asyncFetch()
        sleepAsyncFetch()
        assertEquals(2, count)
    }

    @Test
    fun testInvalidateDedupe() {
        var count = 0
        val b = DedupingBismarck<Int>().fetcher { count++ }
        var sub: Subscription? = null
        sub = b.observeState().subscribe {
            if (it == BismarckState.Fetching) {
                sub!!.unsubscribe()
                b._asyncFetch()
                b.invalidate()
            }
        }
        b._asyncFetch()
        sleepAsyncFetch()
        assertEquals(2, count)
    }
}