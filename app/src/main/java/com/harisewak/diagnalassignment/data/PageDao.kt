package com.harisewak.diagnalassignment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PageDao {

    @Query("SELECT * FROM page WHERE pageNum = (:pageNum)")
    suspend fun getPage(pageNum: String): Page

    @Insert
    suspend fun addAll(pages: List<Page>)

    @Transaction
    @Query("SELECT * FROM page WHERE pageNum = (:pageNum)")
    suspend fun getPageWithContent(pageNum: String): PageWithContents

}