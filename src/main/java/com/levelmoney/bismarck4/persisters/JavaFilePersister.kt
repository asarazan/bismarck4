package com.levelmoney.bismarck4.persisters

import com.levelmoney.bismarck4.Serializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by Aaron Sarazan on 9/7/15
 * Copyright(c) 2015 Level, Inc.
 */
public abstract class JavaFilePersister<R: Any>(val root: String, val serializer: Serializer<R>) : CachingPersister<R>() {

    /**
     * We require this to be dynamically computed because the path is often dependent on userid,
     * and we want to switch over automatically in case of an auth event.
     */
    abstract fun path(): String?

    override fun get(): R? {
        val path = path() ?: return null
        val cached = super.get()
        if (cached != null) return cached
        val file = File(root, "/$path")
        if (!file.exists()) return null
        val loaded = FileInputStream(file).use { serializer.readObject(it) }
        super.put(loaded)
        return loaded
    }

    override fun put(data: R?) {
        val path = path() ?: return
        super.put(data)
        val file = File(root, "/$path")
        if (data == null) {
            file.delete()
        } else {
            val parent = file.parentFile
            if (!parent.exists()) {
                parent.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            FileOutputStream(file).use { serializer.writeObject(it, data) }
        }
    }
}