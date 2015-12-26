package com.levelmoney.bismarck4


/**
 * Created by Aaron Sarazan on 12/26/15.
 * Copyright(c) 2015 Level, Inc.
 */
interface BismarckCenter {

    fun <T : Any> add(bismarck: Bismarck<T>): Bismarck<T>

    fun <T : Any> get(key: String): Bismarck<T>
}