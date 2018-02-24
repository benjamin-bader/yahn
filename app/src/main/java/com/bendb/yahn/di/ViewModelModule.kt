package com.bendb.yahn.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bendb.yahn.feed.FeedViewModel
import com.bendb.yahn.feed.FeedViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindFeedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: FeedViewModelFactory): ViewModelProvider.Factory
}