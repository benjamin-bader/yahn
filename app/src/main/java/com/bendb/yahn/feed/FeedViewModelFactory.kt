package com.bendb.yahn.feed

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class FeedViewModelFactory @Inject constructor (
        private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var provider = providers[modelClass]

        for ((key, value) in providers) {
            if (modelClass.isAssignableFrom(key)) {
                provider = value
                break
            }
        }

        if (provider == null) {
            throw IllegalArgumentException("Unsupported view-model class: $modelClass")
        }

        return provider.get() as T
    }
}