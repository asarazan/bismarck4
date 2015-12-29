package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.BismarckState.*
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by Aaron Sarazan on 12/29/15.
 * Copyright(c) 2015 Level, Inc.
 */
class StateTests {

    @Test
    fun testBasicState() {
        val b = Bismarcks.baseBismarck<String>()
        assertEquals(Stale, b.getState())
        b.insert("Test")
        assertEquals(Fresh, b.getState())
        b.fetcher { throw Fetcher.BismarckFetchError("Some Error") }
        b.invalidate()
        assertEquals(Stale, b.getState())
        b.refresh()
        sleepAsyncFetch()
        assertEquals(Error, b.getState())
    }

    @Test
    fun testStateObserve() {
        var state: BismarckState? = null
        val b = Bismarcks.baseBismarck<String>().fetcher { "Test" }
        b.observeState().subscribe {
            // assert on progression of states observed.
            assertEquals(when (state) {
                null -> Stale
                Stale -> Fetching
                Fetching -> Fresh
                else -> Bismarck
            }, it)
            state = it
        }
        b.refresh()
    }
}