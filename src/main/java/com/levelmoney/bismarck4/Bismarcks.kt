package com.levelmoney.bismarck4

import com.levelmoney.bismarck4.impl.BaseBismarck
import com.levelmoney.bismarck4.impl.DedupingBismarck


/**
 * Created by Aaron Sarazan on 12/28/15.
 * Copyright(c) 2015 Level, Inc.
 */
object Bismarcks {

    @JvmStatic
    fun <T : Any> baseBismarck(): BaseBismarck<T> {
        return BaseBismarck()
    }

    @JvmStatic
    fun <T : Any> dedupingBismarck(): DedupingBismarck<T> {
        return DedupingBismarck()
    }
}