package com.harisewak.diagnalassignment

import android.app.Application
import android.content.Context
import com.harisewak.diagnalassignment.data.MoviesDb
import com.harisewak.diagnalassignment.util.SharedPrefUtil
import com.harisewak.diagnalassignment.util.getPages
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DiagnalApp : Application() {

}