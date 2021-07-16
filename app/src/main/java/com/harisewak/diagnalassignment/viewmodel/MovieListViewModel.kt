package com.harisewak.diagnalassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harisewak.diagnalassignment.data.Content
import com.harisewak.diagnalassignment.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {
    var movieList: List<Content>? = null
    init {
        viewModelScope.launch {
            movieList = repository.provideMovies()
        }
    }

}