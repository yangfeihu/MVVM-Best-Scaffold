package com.xiaojianjun.wanandroid.ui.search.result

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.ext.concat
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.common.CollectRepository

/**
 * Created by yangfeihu on 2019-11-29.
 */
class SearchResultViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val searchResultRepository by lazy { SearchResultRepository() }
    private val collectRepository by lazy { CollectRepository() }

    val articleList = MutableLiveData<MutableList<Article>>()

    val refreshStatus = MutableLiveData<Boolean>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val reloadStatus = MutableLiveData<Boolean>()
    val emptyStatus = MutableLiveData<Boolean>()

    private var currentKeywords = ""
    private var page = INITIAL_PAGE

    fun search(keywords: String = currentKeywords) {
        launch(
            block = {
                if (currentKeywords != keywords) {
                    currentKeywords = keywords
                    articleList.value = emptyList<Article>().toMutableList()
                }

                refreshStatus.value = true
                emptyStatus.value = false
                reloadStatus.value = false

                val pagination = searchResultRepository.search(keywords, INITIAL_PAGE)
                page = pagination.curPage
                articleList.value = pagination.datas
                refreshStatus.value = false
                emptyStatus.value = pagination.datas.isEmpty()
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMore() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = searchResultRepository.search(currentKeywords, page)
                page = pagination.curPage
                articleList.value = articleList.value.concat(pagination.datas)

                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )
    }

    fun collect(id: Long) {
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                updateItemCollectState(id to true)
                Bus.post(USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateItemCollectState(id to false)
            }
        )
    }

    fun uncollect(id: Long) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                updateItemCollectState(id to false)
                Bus.post(USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateItemCollectState(id to true)
            }
        )
    }

    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (isLogin()) {
            val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        articleList.value = list
    }

    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }
}