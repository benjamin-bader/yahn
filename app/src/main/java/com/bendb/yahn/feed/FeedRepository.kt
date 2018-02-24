package com.bendb.yahn.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.support.annotation.MainThread
import com.bendb.yahn.feed.db.FeedDatabase
import com.bendb.yahn.feed.db.transact
import com.bendb.yahn.feed.net.NewsApi
import com.bendb.yahn.feed.net.NewsItemType
import com.bendb.yahn.i
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FeedRepository @Inject constructor(
        private val newsApi: NewsApi,
        private val feedDatabase: FeedDatabase) {

    @MainThread
    fun loadFeed(): FeedModel {
        i { "loadFeed()" }
        val boundaryCallback = FeedBoundaryCallback(newsApi, feedDatabase)
        val dataSourceFactory = feedDatabase.stories().getTopStories()
        val pagedList = LivePagedListBuilder(dataSourceFactory, 30)
                .setBoundaryCallback(boundaryCallback)
                .build()

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) { refresh() }

        return FeedModel(
                stories = pagedList,
                networkState = boundaryCallback.networkState, // todo: boundaryCallback.networkState
                refreshState = refreshState,
                refresh = { refreshTrigger.postValue(null) }
        )
    }

    @MainThread
    private fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>().apply {
            value = NetworkState.LOADING
        }

        newsApi.fetchTopStories()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap { feedDatabase.stories().getMissingStoryIds(it) }
                .flattenAsObservable { it }
                .flatMapSingle {  newsApi.fetchItemById(it) }
                .filter { item -> item.type == NewsItemType.STORY && (item.title?.isNotEmpty() == true) }
                .map(::Story)
                .toList()
                .subscribe { stories, error ->
                    val status = if (error != null) {
                        NetworkState.ERROR
                    } else {
                        feedDatabase.transact {
                            feedDatabase.stories().bulkInsertStories(stories)
                        }
                        NetworkState.LOADED
                    }
                    networkState.postValue(status)
                }

        return networkState
    }
}
