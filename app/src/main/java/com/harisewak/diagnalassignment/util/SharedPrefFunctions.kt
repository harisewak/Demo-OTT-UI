package com.harisewak.diagnalassignment.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

private const val SHARED_PREF_FILE = "DiagnalSharedPref"
private const val IS_DATA_LOADED_IN_DB = "isDataLoaded"

    fun isDataLoadedToDb(context: Context): Boolean {
        return context.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE).getBoolean(IS_DATA_LOADED_IN_DB, false)
    }

    fun setDataLoadedInDb(context: Context) {
        val editor = context.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE).edit()
        editor.putBoolean(IS_DATA_LOADED_IN_DB, true)
        editor.apply()
    }
