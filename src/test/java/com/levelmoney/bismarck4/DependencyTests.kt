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
        sleepAsyncFetch()
        assertEquals(1, count1)
        assertEquals(1, count2)
    }
}