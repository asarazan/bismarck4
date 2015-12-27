package com.levelmoney.bismarck4

import java.io.InputStream
import java.io.OutputStream

/**
 * Created by Aaron Sarazan on 9/25/14
 * Copyright(c) 2014 Level, Inc.
 */
public interface Serializer<T: Any> {
    public fun readObject(stream: InputStream): T?
    public fun writeObject(stream: OutputStream, data: T): Boolean
}
