package com.bendb.yahn.di

import android.arch.persistence.room.Room
import android.content.Context
import com.bendb.yahn.App
import com.bendb.yahn.feed.db.FeedDatabase
import com.bendb.yahn.feed.net.NewsApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    fun provideContext(app: App): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideNewsApi(httpClient: OkHttpClient, moshi: Moshi): NewsApi {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(httpClient)
                .baseUrl("https://hacker-news.firebaseio.com")
                .build()
                .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(app: App): FeedDatabase {
        return Room.databaseBuilder(app, FeedDatabase::class.java, "feed.db")
                .build()
    }

    @Provides
    @Singleton
    @NetworkExecutor
    fun provideNetworkExecutor(): ExecutorService {
        return Executors.newCachedThreadPool()
    }

    @Provides
    @Singleton
    @IoExecutor
    fun provideIoExecutor(): ExecutorService {
        return Executors.newCachedThreadPool()
    }

    @Provides
    @Singleton
    fun provideNetworkScheduler(@NetworkExecutor executorService: ExecutorService): Scheduler {
        return Schedulers.from(executorService)
    }
}