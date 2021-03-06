package com.bendb.yahn.di

import com.bendb.yahn.App
import com.bendb.yahn.feed.FeedActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(app: App): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}

