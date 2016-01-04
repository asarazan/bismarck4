package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.BaseBismarck
import com.levelmoney.bismarck4.persisters.CachingPersister
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Created by Aaron Sarazan on 12/25/15.
 * Copyright(c) 2015 Level, Inc.
 */
class BismarckTests {

    @Test
    fun testBismarckPersistence() {
        val b = BaseBismarck<String>().persister(CachingPersister())
        assertNull(b.peek())
        b.insert("Test")
        assertEquals("Test", b.peek())
        b.insert(null)
        assertNull(b.peek())
    }
}