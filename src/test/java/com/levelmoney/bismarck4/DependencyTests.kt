package com.levelmoney.bismarck4

import org.junit.Test
import kotlin.test.assertEquals


/**
 * Created by Aaron Sarazan on 12/28/15.
 * Copyright(c) 2015 Level, Inc.
 */
class DependencyTests {

    @Test
    fun testCascade() {
        var count1 = 0
        var count2 = 0
        val b1 = Bismarcks.baseBismarck<Int>().fetcher { ++count1 }
        val b2 = Bismarcks.baseBismarck<Int>().fetcher { ++count2 }
        b1.addDependent(b2)
        b1.refresh()
        Thread.sleep(1L)
        assertEquals(1, count1)
        assertEquals(1, count2)
    }

    @Test
    fun testDedupeCascade() {
        var count1 = 0
        var count2 = 0
        val b1 = Bismarcks.dedupingBismarck<Int>().fetcher { ++count1 }
        val b2 = Bismarcks.dedupingBismarck<Int>().fetcher { ++count2 }
        b1.addDependent(b2)
        b1.invalidate()
        Thread.sleep(1L)
        b1.invalidate()
        Thread.sleep(1L)
        assertEquals(2, count1)
        assertEquals(2, count2)
    }
}