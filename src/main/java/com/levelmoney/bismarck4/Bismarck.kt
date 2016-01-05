package com.levelmoney.bismarck4

import rx.Observable
import java.util.concurrent.Executors

/**
 * Created by Aaron Sarazan on 12/25/15.
 * Copyright(c) 2015 Level, Inc.
 */
interface Bismarck<T : Any> {

    companion object {
        val DEFAULT_EXECUTOR = Executors.newCachedThreadPool()
    }

    /**
     * Acquire an rx [Observable] for this data stream.
     */
    fun observe(): Observable<T>

    /**
     * Keep tabs on in-flight fetches.
     */
    fun observeState(): Observable<BismarckState>

    /**
     * Manually set the data of the bismarck.
     */
    fun insert(data: T?)

    /**
     * Make a synchronous fetch call. Should not be done on main thread.
     */
    fun blockingFetch()

    /**
     * Synchronously grab the latest cached version of the data.
     * It's a bit of a smell if you have to use this. You should use [observe] instead.
     */
    fun peek(): T?

    /**
     * Hopefully I can get rid of this eventually, too.
     */
    fun peekState(): BismarckState

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
     * FIFO executed just after data insertion and before dependent invalidation
     */
    fun listen(listener: Listener<T>): Bismarck<T>

    /**
     * Remove a previously added listener
     */
    fun unlisten(listener: Listener<T>): Bismarck<T>

    /**
     * Dependency chaining. Does not detect circular references, so be careful.
     */
    fun addDependent(other: Bismarck<*>): Bismarck<T>

    /**
     * Type-agnostic method for clearing data,
     * since logouts will often cause this to happen in a foreach loop.
     */
    fun clear() = insert(null)
}
