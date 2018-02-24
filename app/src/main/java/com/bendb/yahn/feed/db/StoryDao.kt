package com.bendb.yahn.feed.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bendb.yahn.feed.Story
import io.reactivex.Single

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY _id DESC LIMIT :maxNumToReturn")
    fun getNewestStories(maxNumToReturn: Int = 50): LiveData<List<Story>>

    @Query("SELECT * FROM stories ORDER BY _id DESC")
    fun getTopStories(): DataSource.Factory<Int, Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewStory(story: Story)

    @Query("SELECT _id FROM stories WHERE _id NOT IN (:ids)")
    fun getMissingStoryIds(ids: List<Long>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertStories(stories: List<Story>)
}