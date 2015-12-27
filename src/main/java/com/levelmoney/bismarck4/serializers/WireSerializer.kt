package com.levelmoney.bismarck4.serializers

import com.levelmoney.bismarck4.Serializer
import com.squareup.wire.Message
import com.squareup.wire.Wire
import java.io.*

/**
 * Created by Aaron Sarazan on 9/25/14
 * Copyright(c) 2014 Level, Inc.
 */
public class WireSerializer<R : Message>(val cls: Class<R>) : Serializer<R> {

    override fun writeObject(stream: OutputStream, data: R): Boolean {
        stream.write(data.toByteArray())
        return true
    }

    override fun readObject(stream: InputStream): R? {
        return Wire().parseFrom(stream, cls)
    }

}
