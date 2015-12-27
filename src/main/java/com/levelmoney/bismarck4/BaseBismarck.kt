package com.levelmoney.bismarck4

import rx.Observable


/**
 * Created by Aaron Sarazan on 12/27/15.
 * Copyright(c) 2015 Level, Inc.
 */
abstract class BaseBismarck<T : Any> : Bismarck<T> {

    private val listeners: MutableList<BismarckListener<*>> = arrayListOf()

    abstract fun fetch(): T

    override fun observe(): Observable<T> {
        throw UnsupportedOperationException()
    }

    override fun listen(listener: BismarckListener<T>, position: Int) = apply {
        val index = when (position) {
            Bismarck.POSITION_BEGIN -> 0
            Bismarck.POSITION_END -> listeners.size
            else -> throw IllegalArgumentException("Must specify POSITION_BEGIN or POSITION_END")
        }
        listeners.add(index, listener)
    }

    override fun dependsOn(other: Bismarck<*>): Bismarck<T> {
        throw UnsupportedOperationException()
    }
}