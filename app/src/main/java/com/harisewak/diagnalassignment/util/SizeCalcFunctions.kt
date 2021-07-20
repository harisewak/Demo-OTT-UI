package com.harisewak.diagnalassignment.util

import android.content.res.Resources
import kotlin.math.max
import kotlin.math.min

/*
* calculate w/h aspect ratio required for item
* calculate height for portrait
* calculate height for landscape
* set height programmatically
* */

fun aspectRatio(): Float = 182F / 272F

fun dpToPx(dp: Int) = dp * Resources.getSystem().displayMetrics.density

fun calculateHeight(isPortrait: Boolean): Int {

    val screenWidth = if (isPortrait) {
        min(
            Resources.getSystem().displayMetrics.heightPixels,
            Resources.getSystem().displayMetrics.widthPixels
        )
    } else {
        max(
            Resources.getSystem().displayMetrics.heightPixels,
            Resources.getSystem().displayMetrics.widthPixels
        )
    }

    val height = if (isPortrait) {
        val expectedWidth = ((screenWidth - dpToPx(32)) / 3)
        (expectedWidth / aspectRatio()).toInt()
    } else {
        val expectedWidth = ((screenWidth - dpToPx(64)) / 7)
        (expectedWidth / aspectRatio()).toInt()
    }

    debug("screenWidth: $screenWidth, calculated height: $height")

    return height
}