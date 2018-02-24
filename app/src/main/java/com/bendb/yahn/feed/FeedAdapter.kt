package com.bendb.yahn.feed

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

enum class ChangePayloads {
    SCORE
}

class FeedAdapter : PagedListAdapter<Story, RecyclerView.ViewHolder>(POST_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as StoryViewHolder).bind(getItem(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            // only one payload possible
            (holder as StoryViewHolder).updateScore(getItem(position))
        } else {
            (holder as StoryViewHolder).bind(getItem(position))
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id && oldItem.score == newItem.score
            }

            override fun getChangePayload(oldItem: Story, newItem: Story): Any? {
                if (oldItem.score != newItem.score) {
                    return ChangePayloads.SCORE
                } else {
                    return null
                }
            }
        }
    }
}