package com.bendb.yahn.feed

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bendb.yahn.R

class StoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): StoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_story, parent, false)
            return StoryViewHolder(view)
        }
    }

    private val title: TextView = view.findViewById(R.id.item_story_title)
    private val submitter: TextView = view.findViewById(R.id.item_story_submitter)
    private val url: TextView = view.findViewById(R.id.item_story_url)
    private val score: TextView = view.findViewById(R.id.item_story_score)

    private var story: Story? = null

    init {
        view.setOnClickListener {
            story?.url?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(story: Story?) {
        this.story = story
        title.text = story?.title ?: "loading..."
        submitter.text = story?.submitter ?: "unknown"
        score.text = "${story?.score ?: 0}"
    }

    fun updateScore(story: Story?) {
        this.story = story
        score.text = "${story?.score ?: 0}"
    }
}