package com.pawlowski.stuboard.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pawlowski.stuboard.data.EventsService
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

private const val START_PAGE_INDEX = 1

class EventsPagingSourceFactory @Inject constructor(
    private val eventsService: EventsService,
    private val filters: List<FilterModel>
): PagingSource<Int, EventItemForPreview>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventItemForPreview> {
        val position = params.key ?: START_PAGE_INDEX

        return try {
            val response = eventsService.loadItemPreviews(position, params.loadSize)
            val events = response.getOrThrow()
            val nextKey = if(events.isEmpty())
                null
            else
                position + (params.loadSize / 10)
            LoadResult.Page(
                data = events,
                prevKey = if(position == START_PAGE_INDEX)
                    null
                else
                    position - 1,
                nextKey = nextKey
            )
        }
        catch (exception: IOException)
        {
            return LoadResult.Error(exception)
        }
        catch (exception: HttpException)
        {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EventItemForPreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}