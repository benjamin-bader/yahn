package com.bendb.yahn.feed.net

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Duration
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class NewsApiTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    val api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://hacker-news.firebaseio.com")
            .build()
            .create(NewsApi::class.java)

    @Test fun it_works() {
        val topStoriesResponse = api.fetchTopStories().blockingGet()

        // 16408030
        println(topStoriesResponse)
        //println(api.fetchTopStoriesNewerThan(16408030).blockingGet())
    }

    private fun <L : LiveData<T>, T> L.observeOne(timeout: Duration? = null): T? {
        val resultHolder = ArrayList<T?>(1)
        val latch = CountDownLatch(1)
        lateinit var observer: Observer<T>
        observer = Observer {
            resultHolder.add(it)
            removeObserver(observer)
            latch.countDown()
        }

        observeForever(observer)
        if (timeout != null) {
            latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS)
        } else {
            latch.await()
        }

        return resultHolder[0]
    }
}