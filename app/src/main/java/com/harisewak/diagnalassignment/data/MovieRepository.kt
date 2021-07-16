package com.harisewak.diagnalassignment.data

import com.harisewak.diagnalassignment.util.getPages
import javax.inject.Inject

class MovieRepository @Inject constructor(private val moviesDb: MoviesDb) {

    suspend fun provideMovies(): List<Content> {
        // todo expose data as one page at a time
        return moviesDb.pageDao().getPage("1").contents
    }
}