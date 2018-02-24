package com.bendb.yahn.feed.db

import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.bendb.yahn.feed.net.NewsItemType
import org.threeten.bp.Instant

@TypeConverters
class FeedTypeConverters {
    companion object {
        @JvmStatic @TypeConverter
        fun instantToEpochMillis(instant: Instant): Long {
            return instant.toEpochMilli()
        }

        @JvmStatic @TypeConverter
        fun epochMillisToInstant(epochMillis: Long): Instant {
            return Instant.ofEpochMilli(epochMillis)
        }

        @JvmStatic @TypeConverter
        fun itemTypeToString(type: NewsItemType): String {
            return type.name
        }

        @JvmStatic @TypeConverter
        fun stringToItemType(type: String): NewsItemType {
            return NewsItemType.valueOf(type)
        }
    }
}