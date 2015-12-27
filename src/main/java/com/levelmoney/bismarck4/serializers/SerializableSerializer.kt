package com.levelmoney.bismarck4.serializers

import com.levelmoney.bismarck4.Serializer
import java.io.*

/**
 * Created by Aaron Sarazan on 9/25/14
 * Copyright(c) 2014 Level, Inc.
 */
public class SerializableSerializer<T : Serializable> : Serializer<T> {

    override fun writeObject(stream: OutputStream, data: T): Boolean {
        val oos = ObjectOutputStream(stream)
        oos.writeObject(data)
        oos.close()
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun readObject(stream: InputStream): T? {
        val ois = ObjectInputStream(stream)
        try {
            return ois.readObject() as T
        } catch (e: ClassNotFoundException) {
            throw IOException(e)
        } finally {
            ois.close()
        }
    }
}
