package com.harisewak.diagnalassignment.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.harisewak.diagnalassignment.VISIBLE_THRESHOLD
import com.harisewak.diagnalassignment.data.Content
import com.harisewak.diagnalassignment.data.MovieRepository
import com.harisewak.diagnalassignment.data.MoviesDb
import com.harisewak.diagnalassignment.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private var _movieList: MutableLiveData<List<Content>> = MutableLiveData()

    val movieList: LiveData<List<Content>> = _movieList

    private var _pageTitle: MutableLiveData<String> = MutableLiveData()

    val pageTitle: LiveData<String> = _pageTitle

    @Inject
    lateinit var moviesDb: MoviesDb

    init {
        getMovies()
    }


    fun listScrolled(lastVisibleItemPosition: Int, totalItemCount: Int, query: String) {
        val endOfPage = (totalItemCount - lastVisibleItemPosition) < VISIBLE_THRESHOLD
        if (endOfPage) {
            debug("endOfPage: $endOfPage")
            getMovies(query)
        }
    }

    fun getMovies(query: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val movieList = repository.getMovies(query)
            _movieList.postValue(movieList)
            _pageTitle.postValue(repository.pageTitle)
        }
    }


}