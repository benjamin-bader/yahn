package com.bendb.yahn.feed

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

enum class NetworkState {
    LOADING,
    LOADED,
    ERROR
}

data class FeedModel(
        val stories: LiveData<PagedList<Story>>,
        val networkState: LiveData<NetworkState>,
        val refreshState: LiveData<NetworkState>,
        val refresh: () -> Unit
)