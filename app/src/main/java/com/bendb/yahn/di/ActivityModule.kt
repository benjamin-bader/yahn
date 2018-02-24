package com.bendb.yahn.di

import com.bendb.yahn.feed.FeedActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun feedActivity(): FeedActivity
}