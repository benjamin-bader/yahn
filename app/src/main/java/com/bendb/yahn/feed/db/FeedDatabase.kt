package com.bendb.yahn.feed.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.bendb.yahn.feed.Story


@Database(
        entities = [
            Story::class
        ],
        version = 1
)
@TypeConverters(FeedTypeConverters::class)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun stories(): StoryDao
}

inline fun <D : RoomDatabase, T> D.transact(fn: D.() -> T): T {
    beginTransaction()
    try {
        return this.fn().also {
            setTransactionSuccessful()
        }
    } finally {
        endTransaction()
    }
}
