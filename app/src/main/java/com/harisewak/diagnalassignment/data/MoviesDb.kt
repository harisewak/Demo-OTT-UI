package com.harisewak.diagnalassignment.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Page::class, Content::class), version = 1)
abstract class MoviesDb: RoomDatabase() {
    abstract fun pageDao(): PageDao
}