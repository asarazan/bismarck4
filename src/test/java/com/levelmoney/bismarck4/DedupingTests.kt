package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.DedupingBismarck
import org.junit.Test
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
        Thread.sleep(1L)
        assertEquals(1, count)
        b.invalidate()
        b._asyncFetch()
        Thread.sleep(1L)
        assertEquals(2, count)
    }

    @Test
    fun testInvalidateDedupe() {
        var count = 0
        val b = DedupingBismarck<Int>().fetcher { count++ }
        b._asyncFetch()
        b._asyncFetch()
        b.invalidate()
        Thread.sleep(1L)
        assertEquals(2, count)
    }
}