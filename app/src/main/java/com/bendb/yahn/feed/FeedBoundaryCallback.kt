package com.bendb.yahn.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import com.bendb.yahn.d
import com.bendb.yahn.e
import com.bendb.yahn.feed.db.FeedDatabase
import com.bendb.yahn.feed.db.transact
import com.bendb.yahn.feed.net.NewsApi
import com.bendb.yahn.i
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class FeedBoundaryCallback @Inject constructor(
        private val newsApi: NewsApi,
        private val feedDb: FeedDatabase
) : PagedList.BoundaryCallback<Story>() {

    private val running = AtomicBoolean(false)

    private val mutableNetworkState = MutableLiveData<NetworkState>().apply {
        value = NetworkState.LOADING
    }

    val networkState: LiveData<NetworkState>
        get() = mutableNetworkState

    companion object {
        const val PAGE_SIZE = 50L
    }

    @MainThread
    override fun onZeroItemsLoaded() {
        i { "onZeroItemsLoaded(): Enter" }
        if (!running.compareAndSet(false, true)) {
            // already loading
            i { "onZeroItemsLoaded(): Already active" }
            return
        }

        mutableNetworkState.value = NetworkState.LOADING

        newsApi.fetchTopStories()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flattenAsObservable { it }
                .take(PAGE_SIZE)
                .flatMapSingle { newsApi.fetchItemById(it) }
                .map { Story(it) }
                .toList()
                .subscribe { stories, error ->
                    try {
                        if (error == null) {
                            feedDb.transact { feedDb.stories().bulkInsertStories(stories) }
                            mutableNetworkState.postValue(NetworkState.LOADED)
                        } else {
                            e(error) { "Error loading stories!" }
                            mutableNetworkState.postValue(NetworkState.ERROR)
                        }
                    } catch (e: Exception) {
                        mutableNetworkState.postValue(NetworkState.ERROR)
                        throw e
                    } finally {
                        i { "onZeroItemsLoaded(): Exiting, setting inactive" }
                        running.set(false)
                    }
                }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Story) {
        i { "onItemAtEndLoaded(): Enter" }
        if (!running.compareAndSet(false,true)) {
            i { "onItemAtEndLoaded(): Already active" }
            return
        }

        newsApi.fetchTopStories()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map { it.filter { it < itemAtEnd.id }.sortedDescending() }
                .flattenAsObservable { it }
                .flatMapSingle { newsApi.fetchItemById(it) }
                .filter { it.time < itemAtEnd.submittedAt.epochSecond }
                .take(PAGE_SIZE)
                .map { Story(it) }
                .toList()
                .subscribe { stories, error ->
                    try {
                        if (error == null) {
                            d { "Fetched ${stories.size} story/stories" }
                            feedDb.transact { feedDb.stories().bulkInsertStories(stories) }
                            mutableNetworkState.postValue(NetworkState.LOADED)
                        } else {
                            e(error) { "Error loading stories" }
                            mutableNetworkState.postValue(NetworkState.ERROR)
                        }
                    } catch (e: Exception) {
                        mutableNetworkState.postValue(NetworkState.ERROR)
                        throw e
                    } finally {
                        i { "onItemAtEndLoaded(): Exiting, setting inactive" }
                        running.set(false)
                    }
                }
    }
}