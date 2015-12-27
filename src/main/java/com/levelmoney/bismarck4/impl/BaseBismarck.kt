package com.levelmoney.bismarck4.impl

import com.levelmoney.bismarck4.Bismarck
import com.levelmoney.bismarck4.Listener
import com.levelmoney.bismarck4.Persister
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.Subscriptions


/**
 * Created by Aaron Sarazan on 12/27/15.
 * Copyright(c) 2015 Level, Inc.
 */
abstract class BaseBismarck<T : Any>(val key: String, val persister: Persister<T>) : Bismarck<T> {

    private val listeners: MutableList<Listener<T>> = arrayListOf()
    private val dependents: MutableList<Bismarck<*>> = arrayListOf()

    abstract fun onFetch(): T

    // probably synchronize
    fun fetch(): T {
        return onFetch().apply {
            persister.put(this)
            listeners.forEach {
                it.onUpdate(this)
            }
        }
    }

    override fun observe(): Observable<T> {
        return Observable.create(BismarckOnSubscribe())
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

    private fun cached(): T? {
        return persister.get()
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