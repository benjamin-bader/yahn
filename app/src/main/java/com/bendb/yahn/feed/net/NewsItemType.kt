package com.bendb.yahn.feed.net

import com.squareup.moshi.Json

enum class NewsItemType {
    @Json(name = "job")
    JOB,

    @Json(name = "story")
    STORY,

    @Json(name = "comment")
    COMMENT,

    @Json(name = "poll")
    POLL,

    @Json(name = "pollopt")
    POLLOPT
}