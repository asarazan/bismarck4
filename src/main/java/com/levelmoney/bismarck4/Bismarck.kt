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
     * All Bismarcks must be members of a brain "center".
     * This allows dependencies to be set up,
     * and also allows anonymous lookup by key.
     */
    val center: BismarckCenter?

    /**
     * Acquire an rx [Observable] for this data stream.
     */
    fun observe(): Observable<T>

    /**
     * This can only be called once. Further calls will throw an exception.
     */
    fun bindToCenter(center: BismarckCenter)

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
    fun dependsOn(key: String): Bismarck<T>
}
