package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.data.remote.models.EventsResponse
import com.pawlowski.stuboard.presentation.filters.FilterModel
import retrofit2.Response
import javax.inject.Inject

class EventsServiceFiltersRequestAdapter @Inject constructor(
    private val eventsService: EventsService
): IEventsServiceFiltersRequestAdapter {
    override suspend fun loadItems(
        page: Int,
        pageSize: Int,
        filters: List<FilterModel>
    ): Response<EventsResponse> {
        val isOnlineSelected = filters.filterIsInstance<FilterModel.Place.Online>().isNotEmpty()
        val isRealPlaceSelected = filters.filterIsInstance<FilterModel.Place.RealPlace>().isNotEmpty()
        val isOnline = if(isOnlineSelected && !isRealPlaceSelected)
            true
        else if(!isOnlineSelected && isRealPlaceSelected)
            false
        else
            null

        val selectedRegistrationItems = filters.filterIsInstance<FilterModel.Registration>()
        val isRegistration = if(selectedRegistrationItems.isNotEmpty())
            selectedRegistrationItems.filterIsInstance<FilterModel.Registration.RegistrationNeeded>().isNotEmpty()
        else
            null

        val maxTimeFilters = filters.filterIsInstance<FilterModel.Time.MaxTimeFilter>().ifEmpty { null }
        val maxTimeFilterString = maxTimeFilters?.let {
            maxTimeFilters.map { it.maxTime }.reduce { acc, t ->
                if(t.isAfter(acc))
                    t
                else
                    acc
            }.toString()
        }

        return eventsService.loadItems(
            page = page,
            pageSize = pageSize,
            isOnline = isOnline,
            citiesFilters = filters.filterIsInstance<FilterModel.Place.RealPlace>().map { it.city }.ifEmpty { null },
            tagsFiltersIds = filters.filterIsInstance<FilterModel.Category>().map { it.categoryId }.ifEmpty { null },
            isRegistration = isRegistration,
            endTime = maxTimeFilterString
        )
    }
}