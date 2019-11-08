package com.guardian.podxdemo.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelInjectionFactory
@Inject constructor(
    val viewmodelMultibinding: Map<Class<out ViewModel>,
@JvmSuppressWildcards Provider<ViewModel>>
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewmodelMultibinding[modelClass]
            ?: viewmodelMultibinding.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}