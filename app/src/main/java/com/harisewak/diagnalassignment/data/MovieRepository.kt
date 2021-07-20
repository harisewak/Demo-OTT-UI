package com.harisewak.diagnalassignment.data

import android.content.Context
import com.harisewak.diagnalassignment.MAX_PAGES
import com.harisewak.diagnalassignment.util.getPage
import com.harisewak.diagnalassignment.util.getPages
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

class MovieRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var curPage = 1
    private val movieList = arrayListOf<Content>()
    private var query = ""
    lateinit var pageTitle: String
    private set

    suspend fun getMovies(newQuery: String = ""): List<Content> {
        // when queries change, start fetching from page 1
        if (newQuery != query) {
            query = newQuery
            curPage = 1
            movieList.clear()
        }
        if (curPage <= MAX_PAGES) {
            val page = getPage(context, curPage)
            pageTitle = page.title
            val filteredList = page.contents.filter {
                it.name.contains(query, ignoreCase = true)
            }
            curPage += 1
            return filteredList
        } else {
            return emptyList()
        }
    }

}