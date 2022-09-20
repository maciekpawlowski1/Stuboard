package com.pawlowski.stuboard.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pawlowski.stuboard.data.mappers.toEventItemForPreviewList
import com.pawlowski.stuboard.data.remote.EventsService
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
            val isOnlineSelected = filters.filterIsInstance<FilterModel.Place.Online>().isNotEmpty()
            val isRealPlaceSelected = filters.filterIsInstance<FilterModel.Place.RealPlace>().isNotEmpty()
            val isOnline = if(isOnlineSelected && !isRealPlaceSelected)
                true
            else if(!isOnlineSelected && isRealPlaceSelected)
                false
            else
                null
            val response = eventsService.loadItems(position, params.loadSize, isOnline = isOnline)
            println(response.message())
            println(response.raw().toString())
            val events = response.body()!!.toEventItemForPreviewList()
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
        catch (e: Exception)
        {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EventItemForPreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}