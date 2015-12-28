package com.levelmoney.bismarck4.serializers

import com.levelmoney.bismarck4.Serializer
import com.squareup.wire.Message
import com.squareup.wire.Wire
import java.io.*

/**
 * Created by Aaron Sarazan on 9/25/14
 * Copyright(c) 2014 Level, Inc.
 */
public class WireSerializer<T : Message>(val cls: Class<T>) : Serializer<T> {

    override fun writeObject(stream: OutputStream, data: T): Boolean {
        stream.write(data.toByteArray())
        return true
    }

    override fun readObject(stream: InputStream): T? {
        return Wire().parseFrom(stream, cls)
    }

}
