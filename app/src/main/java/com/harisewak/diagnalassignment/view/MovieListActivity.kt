package com.harisewak.diagnalassignment.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.harisewak.diagnalassignment.LANDSCAPE_COUNT
import com.harisewak.diagnalassignment.PORTRAIT_COUNT
import com.harisewak.diagnalassignment.R
import com.harisewak.diagnalassignment.data.Content
import com.harisewak.diagnalassignment.databinding.ActivityMainBinding
import com.harisewak.diagnalassignment.databinding.ListItemBinding
import com.harisewak.diagnalassignment.util.*
import com.harisewak.diagnalassignment.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel

    private lateinit var mMovieListAdapter: MovieListAdapter

    private lateinit var binding: ActivityMainBinding

    private var isListRefreshed = true

    private var query = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)

        addObservers()

        initAdapter()

        setupScrollListener()

    }

    private fun addObservers() {

        viewModel.pageTitle.observe(this) {
            debug("MovieListActivity.addObservers.pageTitle -> Current thread: ${Thread.currentThread()}")
            binding.toolbar.title = it
        }

        viewModel.movieList.observe(this) { movieList ->
            debug("MovieListActivity.addObservers.movieList -> Current thread: ${Thread.currentThread()}")
            // sending empty list when all pages are exhausted
            if (movieList.isNotEmpty()) {
                if (isListRefreshed) {
                    mMovieListAdapter.clear()
                    mMovieListAdapter.addAll(movieList)
                    isListRefreshed = false
                } else {
                    mMovieListAdapter.addAll(movieList)
                }
            }
        }
    }

    private fun setupScrollListener() {

        val layoutManager = binding.list.layoutManager as GridLayoutManager

        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, query)
            }
        })
    }

    private fun initAdapter(movieList: List<Content> = listOf<Content>()) {

        binding.list.apply {
            val isPortrait = resources.configuration.orientation == 1
            val spanCount = if (isPortrait) PORTRAIT_COUNT else LANDSCAPE_COUNT
            val gridLayoutManager = GridLayoutManager(applicationContext, spanCount)
            layoutManager = gridLayoutManager
            mMovieListAdapter = MovieListAdapter()

            if (movieList.isNotEmpty()) mMovieListAdapter.addAll(movieList)

            adapter = mMovieListAdapter
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        debug("onConfigurationChanged: ${newConfig.orientation}")
        binding.list.removeAllViews()
        val existingItems = mMovieListAdapter.getList()
        initAdapter(existingItems)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchViewItem.actionView as SearchView

        searchView.setOnCloseListener {
            query = ""
            viewModel.getMovies(query)
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovieItems(newText)
                return false
            }

            private fun filterMovieItems(newText: String?) {

                newText?.let {

                    if (newText.length > 2) {
                        isListRefreshed = true
                        query = newText
                        viewModel.getMovies(query)

                    } else if (newText.isEmpty()) {
                        isListRefreshed = true
                        query = ""
                        viewModel.getMovies(query)
                    }
                }
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    class MovieListAdapter() :
        RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

        private val movieList = arrayListOf<Content>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MovieListAdapter.MovieListViewHolder {

            val binding = ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            val layoutParams = binding.ivPoster.layoutParams
            val isPortrait = binding.root.context.resources.configuration.orientation == 1
            layoutParams.height = calculateHeight(isPortrait)
            binding.ivPoster.layoutParams = layoutParams

            return MovieListViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
            val content = movieList[position]
            holder.bind(content)
        }

        fun addAll(newList: List<Content>) {
            if (movieList.size > 0) {
                val lastPos = movieList.lastIndex
                movieList.addAll(newList)
                val newLastPost = movieList.lastIndex
                notifyItemRangeInserted(lastPos + 1, newLastPost)
            } else {
                movieList.addAll(newList)
                notifyItemRangeInserted(0, newList.lastIndex)
            }
        }

        fun clear() {
            movieList.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount() = movieList.size

        fun getList(): List<Content> = movieList

        inner class MovieListViewHolder(private val binding: ListItemBinding) :
            RecyclerView.ViewHolder(
                binding.root
            ) {

            fun bind(content: Content) {
                binding.ivPoster.setImageResource(posterType(content.poster))
                binding.tvName.text = content.name
                binding.tvSerialNum.text = (adapterPosition + 1).toString()
            }

        }
    }

}