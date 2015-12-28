package com.levelmoney.bismarck4.impl

import com.levelmoney.bismarck4.*
import com.levelmoney.bismarck4.persisters.CachingPersister
import com.levelmoney.bismarck4.ratelimit.SimpleRateLimiter
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.Subscriptions
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


/**
 * Created by Aaron Sarazan on 12/27/15.
 * Copyright(c) 2015 Level, Inc.
 */
open class BaseBismarck<T : Any>() : Bismarck<T> {
    companion object {
        private val DEFAULT_EXECUTOR = Executors.newCachedThreadPool()
    }

    private val listeners: MutableList<Listener<T>> = arrayListOf()
    private val dependents: MutableList<Bismarck<*>> = arrayListOf()

    protected var fetcher: Fetcher<T>? = null
        private set
    protected var persister: Persister<T>? = CachingPersister()
        private set
    protected var rateLimiter: RateLimiter? = SimpleRateLimiter(15 * 60 * 1000L)
        private set
    protected var executor: Executor = DEFAULT_EXECUTOR
        private set

    /**
     * Synchronous fetch logic that will be called to fetch/populate our data.
     * Will be scheduled according to the logic in [executor].
     *
     * May be null if you will only be inserting data manually.
     */
    fun fetcher(fetcher: Fetcher<T>?) = apply { this.fetcher = fetcher }

    /**
     * If you want data to actually, you know, persist; then this is your guy.
     *
     * @default: [CachingPersister] a simple in-memory persister
     */
    fun persister(persister: Persister<T>?) = apply { this.persister = persister }

    /**
     * Strategy for determining if data is "fresh".
     * Unfresh data will still be presented, but will trigger a fetch.
     *
     * @default: [SimpleRateLimiter] data is fresh for 15 minutes or until manually invalidated.
     */
    fun rateLimiter(rateLimiter: RateLimiter?) = apply { this.rateLimiter = rateLimiter }

    /**
     * You should probably leave this alone-- but if you want to hand-tune thread allocations, knock yourself out.
     *
     * @default: shared instance of [Executors.newCachedThreadPool]
     */
    fun executor(executor: Executor) = apply { this.executor = executor }

    override fun blockingFetch() {
        fetcher?.onFetch()?.apply { insert(this) }
    }

    private fun asyncFetch() {
        executor.execute { blockingFetch() }
    }

    override fun observe(): Observable<T> {
        return Observable.create(BismarckOnSubscribe()).apply {
            if (!isFresh()) {
                asyncFetch()
            }
        }
    }

    override fun insert(data: T?) {
        val old = cached()
        persister?.put(data)
        rateLimiter?.update()
        listeners.forEach {
            it.onUpdate(data)
        }
        if (old != data) {
            dependents.forEach { it.invalidate() }
        }
    }

    override fun isFresh(): Boolean {
        return rateLimiter?.isFresh() ?: false
    }

    override fun invalidate() {
        rateLimiter?.reset()
        asyncFetch()
        dependents.forEach { it.invalidate() }
    }

    override fun listen(listener: Listener<T>, position: Int) = apply {
        val index = when (position) {
            Bismarck.POSITION_BEGIN -> 0
            Bismarck.POSITION_END -> listeners.size
            else -> throw IllegalArgumentException("Must specify POSITION_BEGIN or POSITION_END")
        }
        listeners.add(index, listener)
    }

    override fun unlisten(listener: Listener<T>) {
        listeners.remove(listener)
    }

    override fun addDependent(other: Bismarck<*>) = apply {
        dependents.add(other)
    }

    internal fun cached(): T? {
        return persister?.get()
    }

    private inner class BismarckOnSubscribe : Observable.OnSubscribe<T> {
        override fun call(sub: Subscriber<in T>) {
            if (sub.isUnsubscribed) return
            val listener = object : Listener<T> {
                override fun onUpdate(data: T?) {
                    sub.onNext(data ?: return)
                }
            }
            sub.add(Subscriptions.create { unlisten(listener) })
            sub.onStart()
            cached()?.let { sub.onNext(it) }
        }
    }
}