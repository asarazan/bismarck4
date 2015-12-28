package com.levelmoney.bismarck4

import org.junit.Test
import kotlin.test.assertEquals


/**
 * Created by Aaron Sarazan on 12/28/15.
 * Copyright(c) 2015 Level, Inc.
 */
class ListenerTests {

    @Test
    fun testFrontAndBack() {
        var someInt = 0
        val b = Bismarcks.baseBismarck<String>().listen { someInt = 1 }.listenFront { someInt = -1 }
        b.insert("Test")
        assertEquals(1, someInt)
    }
}