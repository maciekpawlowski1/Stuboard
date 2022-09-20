package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.data.remote.models.EventAddModel
import com.pawlowski.stuboard.data.remote.models.EventsResponse
import com.pawlowski.stuboard.data.remote.models.EventsResponseItem
import retrofit2.Response
import retrofit2.http.*

interface EventsService {
    @GET("/api/Events/GetAll")
    suspend fun loadItems(@Query("PageNumber") page: Int, @Query("PageSize") pageSize: Int): Response<EventsResponse>

    @GET("api/Events/getbyid/{id}")
    suspend fun loadItemById(@Path("id") eventId: String): Response<EventsResponseItem>

    @POST("api/Events")
    suspend fun addNewEvent(@Body event: EventAddModel): Response<String>
}