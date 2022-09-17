package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

class FakeEventsService {
    private val fakeNumbers = (1..100).toList()
    private val tryCount = AtomicInteger(0)

    suspend fun loadItems(
        page: Int,
        pageSize: Int
    ): Result<List<EventItemForPreview>> {
//        delay(1000)
//        if(true)
//            return Result.success(listOf())
        val startIndex = (page-1)*pageSize
        val endIndex = if(page*pageSize <= 100)
            page*pageSize
        else 100

        delay(3000)
        val isError = tryCount.getAndIncrement()%3 == 1

        if(isError)
            return Result.failure(okio.IOException())
        return if(startIndex <= 100)
            Result.success(fakeNumbers.subList(startIndex, endIndex).map {
            EventItemForPreview(
                eventId = it,
                tittle = "Wydarzenie nr $it",
                place = "Kraków, Reymonta $it",
                dateDisplayString = "$it września",
                isFree = (it % 2) == 1,
                imageUrl = PreviewUtils.defaultEventPreviews[it%PreviewUtils.defaultEventPreviews.size].imageUrl
            )
        })
        else
            Result.success(listOf())
    }


}