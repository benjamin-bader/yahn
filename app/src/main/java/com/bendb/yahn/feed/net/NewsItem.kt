package com.bendb.yahn.feed.net

open class NewsItem {
    var id: Long = -1

    var deleted: Boolean = false

    var type: NewsItemType = NewsItemType.STORY

    var by: String? = null

    /**
     * Creation time, in seconds since the UNIX epoch.
     */
    var time: Long = 0

    var text: String? = null

    var dead: Boolean = false

    /**
     * For comments, either another comment or the story itself.
     */
    var parent: Long? = null

    /**
     * For poll options, the associated poll.
     */
    var poll: Long? = null

    /**
     * The IDs of the item's comments, in ranked display order.
     */
    var kids: List<Long> = mutableListOf()

    /**
     * The URL of the story.
     */
    var url: String? = null

    /**
     * The story's score, or votes for a pollopt.
     */
    var score: Long? = null

    /**
     * The title of a story, poll, or job.
     */
    var title: String? = null

    /**
     * For polls, the IDs of their options.
     */
    var parts: List<Long>? = null

    /**
     * For stories and polls, the total comment count.
     */
    var descendants: Long? = null
}