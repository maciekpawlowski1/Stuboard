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

        val selectedRegistrationItems = filters.filterIsInstance<FilterModel.Registration>()
        val isRegistration = if(selectedRegistrationItems.isNotEmpty())
        {
            val single = selectedRegistrationItems.singleOrNull()
            single?.let {
                it is FilterModel.Registration.RegistrationNeeded
            }
        }
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

        val tickets = filters
            .filterIsInstance<FilterModel.EntryPrice>()
            .map {
                it is FilterModel.EntryPrice.Paid
            }
            .singleOrNull()

        val textFilter = filters.filterIsInstance<FilterModel.CustomTextFilter>()
            .map { it.customText }
            .firstOrNull()

        return eventsService.loadItems(
            page = page,
            pageSize = pageSize,
            isOnline = isOnlineSelected,
            citiesFilters = filters.filterIsInstance<FilterModel.Place.RealPlace>().map { it.city }
                .ifEmpty { null },
            tagsFiltersIds = filters.filterIsInstance<FilterModel.Category>().map { it.categoryId }
                .ifEmpty { null },
            isRegistration = isRegistration,
            endTime = maxTimeFilterString,
            tickets = tickets,
            textSearch = textFilter
        )
    }
}