package com.harisewak.diagnalassignment.util

import android.content.Context
import com.harisewak.diagnalassignment.R
import com.harisewak.diagnalassignment.data.Content
import com.harisewak.diagnalassignment.data.Page
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun getPages(context: Context): List<Page> {
    val result = arrayListOf<Page>()

    val page1 = read(context, "page1.json").toJson().toPage()
    val page2 = read(context, "page2.json").toJson().toPage()
    val page3 = read(context, "page3.json").toJson().toPage()

    result.add(page1)
    result.add(page2)
    result.add(page3)

    return result
}

private fun read(context: Context, fileName: String): String {

    return try {
        val input = context.assets.open(fileName)
        val size = input.available()
        val buffer = ByteArray(size)
        input.read(buffer)
        input.close()
        String(buffer)

    } catch (e: IOException) {
        e.printStackTrace()
        return ""
    }

}

private fun String.toJson(): JSONObject {
    return JSONObject(this)
}

private fun JSONObject.toPage(): Page {

    val pageJson = getJSONObject("page")
    val items = pageJson.getJSONObject("content-items").getJSONArray("content").toList()

    return Page(
        title = pageJson.getString("title"),
        total = pageJson.getString("total-content-items"),
        pageNum = pageJson.getString("page-num"),
        pageSize = pageJson.getString("page-size"),
        contents = items,
        pageId = 0
    )
}

private fun JSONArray.toList(): ArrayList<Content> {
    val result = ArrayList<Content>()

    for (i in 0 until this.length()) {
        val json = this.getJSONObject(i)
        val content = Content(
            name = json.getString("name"),
            poster = json.getString("poster-image"),
            contentId = 0,
            parentPageId = 0
        )
        result.add(content)
    }

    return result
}

fun posterType(string: String): Int {
    return when (string) {
        "poster1.jpg" -> R.drawable.poster1
        "poster2.jpg" -> R.drawable.poster2
        "poster3.jpg" -> R.drawable.poster3
        "poster4.jpg" -> R.drawable.poster4
        "poster5.jpg" -> R.drawable.poster5
        "poster6.jpg" -> R.drawable.poster6
        "poster7.jpg" -> R.drawable.poster7
        "poster8.jpg" -> R.drawable.poster8
        "poster9.jpg" -> R.drawable.poster9
        else -> R.drawable.placeholder_for_missing_posters
    }
}
