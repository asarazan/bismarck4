package com.levelmoney.bismarck4.serializers

import com.google.gson.Gson
import com.levelmoney.bismarck4.Serializer
import java.io.*

/**
 * Created by Aaron Sarazan on 9/25/14
 * Copyright(c) 2014 Level, Inc.
 */
public class GsonSerializer<T: Any>(val cls: Class<T>, val gson: Gson) : Serializer<T> {

    override fun writeObject(stream: OutputStream, data: T): Boolean {
        stream.write(gson.toJson(data).toByteArray())
        return true
    }

    override fun readObject(stream: InputStream): T? {
        val bytes = ByteArray(stream.available())
        stream.read(bytes)
        return gson.fromJson(String(bytes), cls)
    }
}
