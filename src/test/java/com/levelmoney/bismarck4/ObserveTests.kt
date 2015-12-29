package com.levelmoney.bismarck4

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by Aaron Sarazan on 12/29/15.
 * Copyright(c) 2015 Level, Inc.
 */
class ObserveTests {

    @Test
    fun testBasicObserve() {
        var listened = ""
        val b = Bismarcks.baseBismarck<String>()
        b.observe().subscribe {
            listened = it
        }
        b.insert("Test")
        assertEquals("Test", listened)
    }
}