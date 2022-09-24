package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.data.remote.models.EventsResponse
import com.pawlowski.stuboard.presentation.filters.FilterModel
import retrofit2.Response

interface IEventsServiceFiltersRequestAdapter {
    suspend fun loadItems(
        page: Int,
        pageSize: Int,
        filters: List<FilterModel>
    ): Response<EventsResponse>
}