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
     * Manually set the data of the bismarck.
     */
    fun insert(data: T?)

    /**
     * Make a synchronous fetch call. Should not be done on main thread.
     */
    fun blockingFetch()

    /**
     * The bismarck will usually employ some sort of timer or hash comparison to determine this.
     * Can also call [invalidate] to force this to false.
     */
    fun isFresh(): Boolean

    /**
     * Should cause [isFresh] to return false.
     */
    fun invalidate()

    /**
     * Trigger asyncFetch of this and all dependencies where [isFresh] is false.
     */
    fun refresh()

    /**
     * Usually used to notify UI and external observers. Can also inject logging.
     * Stored as an ordered FIFO list
     * @param listener the [BismarckListener] to invoke
     * @param position appends by default, can prepend with [POSITION_BEGIN]
     */
    fun listen(listener: Listener<T>, position: Int = POSITION_END): Bismarck<T>

    /**
     * Find listener and remove it from list. It will receive no more calls from Bismarck.
     */
    fun unlisten(listener: Listener<T>)

    /**
     * Dependency chaining. Does not detect circular references, so be careful.
     */
    fun addDependent(other: Bismarck<*>): Bismarck<T>
}
