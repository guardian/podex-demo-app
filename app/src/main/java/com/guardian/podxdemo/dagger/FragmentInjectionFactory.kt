package com.guardian.podxdemo.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class FragmentInjectionFactory
@Inject constructor(
    private val fragmentMultiBinding: Map<Class<out Fragment>,
@JvmSuppressWildcards Provider<Fragment>>
) :
    FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentProvider = fragmentMultiBinding[loadFragmentClass(classLoader, className)]
        try {
            return fragmentProvider?.get() ?: super.instantiate(classLoader, className)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}