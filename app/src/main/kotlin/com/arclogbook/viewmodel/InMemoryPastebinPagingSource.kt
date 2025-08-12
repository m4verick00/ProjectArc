package com.arclogbook.viewmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arclogbook.pastebin.PastebinAlert

class InMemoryPastebinPagingSource(private val provider: () -> List<PastebinAlert>) : PagingSource<Int, PastebinAlert>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PastebinAlert> = try {
        val page = params.key ?: 0
        val data = provider()
        val from = page * params.loadSize
        val to = (from + params.loadSize).coerceAtMost(data.size)
        val slice = if (from < to) data.subList(from, to) else emptyList()
        LoadResult.Page(
            data = slice,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (to < data.size) page + 1 else null
        )
    } catch (e: Exception) { LoadResult.Error(e) }
    override fun getRefreshKey(state: PagingState<Int, PastebinAlert>): Int? = 0
}
