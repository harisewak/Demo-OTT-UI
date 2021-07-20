package com.harisewak.diagnalassignment.data

import androidx.room.*

@Entity
data class Page @JvmOverloads constructor(
    val title: String,
    val total: String,
    val pageNum: String,
    val pageSize: String,
    @Ignore var contents: List<Content> = arrayListOf(),
    @PrimaryKey(autoGenerate = true) val pageId: Long
)

@Entity
data class Content(
    @PrimaryKey(autoGenerate = true) val contentId: Long,
    val parentPageId: Long,
    val name: String,
    val poster: String
)

data class PageWithContents(
    @Embedded val page: Page,
    @Relation(
        parentColumn = "pageId",
        entityColumn = "parentPageId"
    )
    val contents: List<Content>
)
