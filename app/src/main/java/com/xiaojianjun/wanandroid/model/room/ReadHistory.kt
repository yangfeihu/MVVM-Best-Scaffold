package com.xiaojianjun.wanandroid.model.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Tag

/**
 * Created by yangfeihu on 2019-12-05.
 */
@Entity
data class ReadHistory(
    @Embedded
    var article: Article,
    @Relation(
        parentColumn = "id",
        entityColumn = "articleId",
    )
    var tags: MutableList<Tag>
)