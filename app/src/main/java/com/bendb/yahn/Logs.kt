package com.bendb.yahn

import timber.log.Timber

inline fun logIfEnabled(action: () -> Unit) {
    if (Timber.treeCount() > 0) {
        action()
    }
}

inline fun v(message:() -> String) {
    logIfEnabled { Timber.v(message()) }
}

inline fun d(message: () -> String) {
    logIfEnabled { Timber.d(message()) }
}

inline fun i(message: () -> String) {
    logIfEnabled { Timber.i(message()) }
}

inline fun w(message: () -> String) {
    logIfEnabled { Timber.w(message()) }
}

inline fun e(message: () -> String) {
    logIfEnabled { Timber.e(message()) }
}

inline fun e(error: Throwable, message: () -> String) {
    logIfEnabled { Timber.e(error, message()) }
}