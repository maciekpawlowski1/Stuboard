package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.data.remote.models.EventsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsService {
    @GET("/api/Events/GetAll")
    suspend fun loadItems(@Query("PageNumber") page: Int, @Query("PageSize") pageSize: Int): Response<EventsResponse>

    //suspend fun loadItemById(eventId: String): Result<List<EventItemForPreview>>
}