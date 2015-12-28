package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.BaseBismarck
import com.levelmoney.bismarck4.persisters.CachingPersister
import com.levelmoney.bismarck4.persisters.JavaFilePersister
import com.levelmoney.bismarck4.serializers.SerializableSerializer
import org.junit.Assert.*
import org.junit.Test
import java.io.File


/**
 * Created by Aaron Sarazan on 12/25/15.
 * Copyright(c) 2015 Level, Inc.
 */
class PersisterTests {

    @Test
    fun testCached() {
        val persister = CachingPersister<String>()
        assertNull(persister.get())
        persister.put("Test")
        assertEquals(persister.get(), "Test")
    }

    @Test
    fun testJavaFile() {
        val persister = object : JavaFilePersister<String>("/tmp", SerializableSerializer<String>()) {
            override fun path(): String? {
                return "test"
            }
        }
        assertNull(persister.get())
        persister.put("Test")
        assertEquals("Test", persister.get())
        persister.cached = null
        assertEquals("Test", persister.get())
        assertTrue(File("${persister.root}/${persister.path()}").delete())
        assertEquals("Test", persister.get())
        persister.cached = null
        assertNull(persister.get())
    }

    @Test
    fun testBismarckPersistence() {
        val b = BaseBismarck<String>().persister(CachingPersister())
        assertNull(b.cached())
        b.insert("Test")
        assertEquals("Test", b.cached())
    }
}