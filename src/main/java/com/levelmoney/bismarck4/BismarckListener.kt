package com.levelmoney.bismarck4;

/**
 * Created by Aaron Sarazan on 12/26/15.
 * Copyright(c) 2015 Level, Inc.
 */
interface BismarckListener<T : Any> {
    fun onUpdate(data: T)
}