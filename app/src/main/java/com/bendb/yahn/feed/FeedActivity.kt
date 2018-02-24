package com.bendb.yahn.feed

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.bendb.yahn.R
import com.bendb.yahn.i
import com.bendb.yahn.observe
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FeedActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: FeedViewModelFactory

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        adapter = FeedAdapter()

        swipeRefreshLayout = findViewById(R.id.main_refresh)
        recycler = findViewById(R.id.main_recycler)
        recycler.adapter = adapter

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel::class.java)
        viewModel.stories.observe(this) {
            adapter.setList(it)
        }

        viewModel.refreshState.observe(this) {
            i { "New refresh state: $it" }
            swipeRefreshLayout.isRefreshing = when (it) {
                null,
                NetworkState.LOADED,
                NetworkState.ERROR -> false
                NetworkState.LOADING -> true
                else -> error { "Unexpected NetworkState value: $it" }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loadFeed()
    }
}
