package com.levelmoney.bismarck4.impl

import com.levelmoney.bismarck4.*
import com.levelmoney.bismarck4.persisters.CachingPersister
import com.levelmoney.bismarck4.ratelimit.SimpleRateLimiter
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.Subscriptions


/**
 * Created by Aaron Sarazan on 12/27/15.
 * Copyright(c) 2015 Level, Inc.
 */
class BaseBismarck<T : Any>() : Bismarck<T> {

    private val listeners: MutableList<Listener<T>> = arrayListOf()
    private val dependents: MutableList<Bismarck<*>> = arrayListOf()

    private var fetcher: Fetcher<T>? = null
    fun fetcher(fetcher: Fetcher<T>?) = apply { this.fetcher = fetcher }

    private var persister: Persister<T>? = CachingPersister()
    fun persister(persister: Persister<T>?) = apply { this.persister = persister }

    private var rateLimiter: RateLimiter? = SimpleRateLimiter(15 * 60 * 1000L)
    fun rateLimiter(rateLimiter: RateLimiter?) = apply { this.rateLimiter = rateLimiter }

    override fun blockingFetch() {
        fetcher?.onFetch()?.apply { insert(this) }
    }

    override fun observe(): Observable<T> {
        return Observable.create(BismarckOnSubscribe()).apply {
            if (!isFresh()) {
                BismarckFetching.add(this@BaseBismarck)
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
        BismarckFetching.add(this)
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