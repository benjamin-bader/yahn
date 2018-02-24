package com.bendb.yahn

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread

@MainThread
fun <L : LiveData<T>, T> L.observe(owner: LifecycleOwner, onChanged: (T?) -> Unit) {
    this.observe(owner, Observer<T> { value -> onChanged(value) })
}
