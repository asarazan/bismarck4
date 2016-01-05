package com.levelmoney.bismarck4.impl

import com.levelmoney.bismarck4.*
import com.levelmoney.bismarck4.persisters.CachingPersister
import com.levelmoney.bismarck4.ratelimit.SimpleRateLimiter
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.Subscriptions
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by Aaron Sarazan on 12/27/15.
 * Copyright(c) 2015 Level, Inc.
 */
open class BaseBismarck<T : Any>() : Bismarck<T> {

    // Because the [synchronized] calls were breaking and I'm lazy
    private val listeners               = CopyOnWriteArrayList<Listener<T>>()
    private val subscribers             = CopyOnWriteArrayList<Subscriber<in T>>()
    private val stateSubscribers        = CopyOnWriteArrayList<Subscriber<in BismarckState>>()
    private val dependents              = CopyOnWriteArrayList<Bismarck<*>>()

    private var fetchCount              = AtomicInteger(0)
    private var lastError: Throwable?   = null

    protected var fetcher: Fetcher<T>? = null
        private set
    protected var persister: Persister<T>? = CachingPersister()
        private set
    protected var rateLimiter: RateLimiter? = SimpleRateLimiter(15 * 60 * 1000L)
        private set
    protected var executor: Executor = Bismarck.DEFAULT_EXECUTOR
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

    override final fun blockingFetch() {
        fetchCount.incrementAndGet()
        updateState()
        try {
            fetcher?.onFetch()?.apply {
                lastError = null
                insert(this)
            }
        } catch (e: Fetcher.BismarckFetchError) {
            lastError = e
        } finally {
            fetchCount.decrementAndGet()
            updateState()
        }
    }

    protected open fun asyncFetch() {
        executor.execute { blockingFetch() }
    }

    override fun observe(): Observable<T> {
        return Observable.create(BismarckOnSubscribe()).apply {
            if (!isFresh()) {
                asyncFetch()
            }
        }
    }

    override fun observeState(): Observable<BismarckState> {
        return Observable.create(StateOnSubscribe())
    }

    override fun insert(data: T?) {
        val old = peek()
        persister?.put(data)
        rateLimiter?.update()
        listeners.forEachCompat {
            it.onUpdate(data)
        }
        subscribers.forEachCompat {
            it.onNext(data)
        }
        if (old != data) {
            dependents.forEachCompat { it.invalidate() }
        }
    }

    override fun isFresh(): Boolean {
        return rateLimiter?.isFresh() ?: false
    }

    override fun invalidate() {
        rateLimiter?.reset()
        dependents.forEachCompat { it.invalidate() }
    }

    override fun refresh() {
        if (!isFresh()) { asyncFetch() }
        dependents.forEachCompat { it.refresh() }
    }

    override fun listen(listener: Listener<T>) = apply {
        listeners.add(listener)
    }

    override fun unlisten(listener: Listener<T>) = apply {
        listeners.remove(listener)
    }

    override fun addDependent(other: Bismarck<*>) = apply {
        dependents.add(other)
    }

    private fun updateState() {
        val state = peekState()
        stateSubscribers.forEachCompat {
            it.onNext(state)
        }
    }

    override fun peekState(): BismarckState {
        return when {
            fetchCount.get() > 0    -> BismarckState.Fetching
            lastError != null       -> BismarckState.Error
            isFresh()               -> BismarckState.Fresh
            else                    -> BismarckState.Stale
        }
    }

    override fun peek(): T? {
        return persister?.get()
    }

    private inner class StateOnSubscribe : Observable.OnSubscribe<BismarckState> {
        override fun call(sub: Subscriber<in BismarckState>) {
            if (sub.isUnsubscribed) return
            stateSubscribers.add(sub)
            sub.add(Subscriptions.create { stateSubscribers.remove(sub) })
            sub.onStart()
            sub.onNext(peekState())
        }
    }

    private inner class BismarckOnSubscribe : Observable.OnSubscribe<T> {
        override fun call(sub: Subscriber<in T>) {
            if (sub.isUnsubscribed) return
            subscribers.add(sub)
            sub.add(Subscriptions.create { subscribers.remove(sub) })
            sub.onStart()
            peek()?.let { sub.onNext(it) }
        }
    }
}