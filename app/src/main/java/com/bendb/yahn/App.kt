package com.bendb.yahn

import android.app.Activity
import android.app.Application
import com.bendb.yahn.di.DaggerAppComponent
import com.gabrielittner.threetenbp.LazyThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject
import kotlin.concurrent.thread

class App : Application(), HasActivityInjector {

    @Inject lateinit var injector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = injector

    override fun onCreate() {
        super.onCreate()

        LazyThreeTen.init(this)
        thread {
            LazyThreeTen.cacheZones()
        }

        Timber.plant(Timber.DebugTree())

        i { "HELLO WORLD" }

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }
}