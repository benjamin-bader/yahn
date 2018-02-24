package com.bendb.yahn.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import javax.inject.Inject

class FeedViewModel @Inject constructor(
        private val feedRepository: FeedRepository
) : ViewModel() {
    private val poke = MutableLiveData<Unit>()
    private val feedModelResult = Transformations.map(poke) { feedRepository.loadFeed() }

    val stories: LiveData<PagedList<Story>> = Transformations.switchMap(feedModelResult) { it.stories }
    val refreshState = Transformations.switchMap(feedModelResult) { it.refreshState }

    fun loadFeed() {
        poke.value = null
    }

    @MainThread
    fun refresh() {
        feedModelResult?.value?.refresh?.invoke()
        //poke.value = null
    }
}