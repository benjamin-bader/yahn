package com.bendb.yahn.feed.net

import android.arch.lifecycle.LiveData
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {
    @GET("/v0/item/{id}.json")
    fun fetchItemById(@Path("id") id: Long): Single<NewsItem>

    @GET("/v0/topstories.json")
    fun fetchTopStories(): Single<List<Long>>

    @GET("/v0/topstories.json?orderBy=\"\$key\"")
    fun fetchTopStoriesNewerThan(@Query("startAt") id: Long): Single<List<Long>>

    @GET("/v0/newstories.json")
    fun fetchNewestStories(): Single<List<Long>>

    @GET("/v0/maxitem.json")
    fun fetchMaxItem(): Single<Long>
}