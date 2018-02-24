package com.bendb.yahn.feed

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.bendb.yahn.feed.net.NewsItem
import com.bendb.yahn.feed.net.NewsItemType
import com.squareup.moshi.Json
import org.threeten.bp.Instant

@Entity(
        tableName = "stories"
)
class Story() {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    var id: Long = -1

    @ColumnInfo(name = "type")
    var type: NewsItemType = NewsItemType.STORY

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "url")
    var url: String? = null

    @ColumnInfo(name = "submitted_at")
    var submittedAt: Instant = Instant.EPOCH

    @ColumnInfo(name = "is_new")
    var isNew: Boolean = false

    @ColumnInfo(name = "submitter")
    var submitter: String = ""

    @ColumnInfo(name = "score")
    var score: Int = -1

    @Ignore
    constructor(id: Long, type: NewsItemType, title: String, url: String?) : this() {
        this.id = id
        this.type = type
        this.title = title
        this.url = url
    }

    @Ignore
    constructor(item: NewsItem) : this() {
        id = item.id
        type = item.type
        title = item.title ?: ""
        url = item.url ?: ""
        submittedAt = Instant.ofEpochSecond(item.time)
        isNew = true
        submitter = item.by ?: ""
        score = item.score?.toInt() ?: 0
    }
}