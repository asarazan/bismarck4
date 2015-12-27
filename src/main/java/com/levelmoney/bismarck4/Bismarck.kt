package com.levelmoney.bismarck4

import rx.Observable

/**
 * Created by Aaron Sarazan on 12/25/15.
 * Copyright(c) 2015 Level, Inc.
 */
interface Bismarck<T : Any> {

    companion object {
        val POSITION_BEGIN = 1
        val POSITION_END = 2
    }

    /**
     * Acquire an rx [Observable] for this data stream.
     */
    fun observe(): Observable<T>

    /**
     * Usually used to notify UI and external observers. Can also inject logging.
     * Stored as an ordered FIFO list
     * @param listener the [BismarckListener] to invoke
     * @param position appends by default, can prepend with [POSITION_BEGIN]
     */
    // TODO [JvmOverloads] once Kobalt is fixed.
    fun listen(listener: BismarckListener<T>, position: Int = POSITION_END): Bismarck<T>

    /**
     * Dependency chaining. Does not detect circular references, so be careful.
     * This, by default, will inject network fetches when an upstream invalidation is called.
     */
    fun dependsOn(other: Bismarck<*>): Bismarck<T>
}
