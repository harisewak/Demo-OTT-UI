package com.harisewak.diagnalassignment.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    fun provideDb(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        MoviesDb::class.java, "movies-db"
    ).build()

}