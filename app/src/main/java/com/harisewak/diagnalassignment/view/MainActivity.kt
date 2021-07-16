package com.harisewak.diagnalassignment.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harisewak.diagnalassignment.R
import com.harisewak.diagnalassignment.data.Content
import com.harisewak.diagnalassignment.data.MoviesDb
import com.harisewak.diagnalassignment.databinding.ActivityMainBinding
import com.harisewak.diagnalassignment.databinding.ListItemBinding
import com.harisewak.diagnalassignment.util.SharedPrefUtil
import com.harisewak.diagnalassignment.util.getPages
import com.harisewak.diagnalassignment.util.posterType
import com.harisewak.diagnalassignment.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var moviesDb: MoviesDb
    private lateinit var viewModel: MovieListViewModel
    private val mMovieListAdapter = MovieListAdapter()

    private lateinit var binding: ActivityMainBinding
    private val screenWidth by lazy {
        val displayMetrics = DisplayMetrics()

        val display =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                applicationContext.display?.getRealMetrics(displayMetrics)
            } else {
                windowManager.defaultDisplay.getMetrics(displayMetrics)
            }

        displayMetrics.widthPixels
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // push data from assets to db during first launch
        if (!SharedPrefUtil.isDataLoadedToDb(applicationContext)) {
            GlobalScope.launch {
                moviesDb.pageDao().addAll(getPages(applicationContext))
                SharedPrefUtil.setDataLoadedInDb(applicationContext)
                Log.d(TAG, "Data loaded into DB successfully")
            }
        } else {
            viewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
            viewModel.movieList?.let { mMovieListAdapter.submitList(it) }
            binding.list.apply {
                adapter = mMovieListAdapter
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchViewItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (newText.length > 2) {
                        val filteredList = viewModel.movieList?.filter {
                            it.name.contains(newText, ignoreCase = true)
                        }
                        filteredList?.let { it1 -> mMovieListAdapter.submitList(it1) }
                    } else {
                        viewModel.movieList?.let { it1 -> mMovieListAdapter.submitList(it1) }
                    }
                }

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    class MovieListAdapter() : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Content>() {

            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
                // todo if required, add ID check
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
                // todo if required, implement equals in Content class
                return oldItem == newItem
            }

        }

        private val mDiffer: AsyncListDiffer<Content> = AsyncListDiffer(this, DIFF_CALLBACK)

        fun submitList(list: List<Content>) = mDiffer.submitList(list)


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.MovieListViewHolder {
            val binding = ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MovieListViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
            val content = mDiffer.currentList[position]
            holder.bind(content)
        }

        override fun getItemCount() = mDiffer.currentList.size

        inner class MovieListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(content: Content) {
                binding.ivPoster.setImageResource(posterType(content.poster))
                binding.tvName.text = content.name
            }

        }
    }

}