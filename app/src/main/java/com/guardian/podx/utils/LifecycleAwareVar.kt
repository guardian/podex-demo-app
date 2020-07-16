package com.guardian.podx.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A property that gets cleaned up when the fragment is destroyed.
 *
 * Accessing this variable in a destroyed fragment will throw an exception.
 */
class LifecycleAwareVar <T : Any>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {
    private var value: T? = null

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                value = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return value ?: throw IllegalStateException()
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        this.value = value
    }
}

/**
 * Creates an [LifecycleAwareVar] associated with this fragment.
 */
fun <T : Any> Fragment.lifecycleAwareVar() = LifecycleAwareVar<T>(this)