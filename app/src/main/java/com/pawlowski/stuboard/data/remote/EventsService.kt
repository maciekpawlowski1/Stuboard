package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.data.remote.models.EventAddModel
import com.pawlowski.stuboard.data.remote.models.EventsResponse
import com.pawlowski.stuboard.data.remote.models.EventsResponseItem
import retrofit2.Response
import retrofit2.http.*

interface EventsService {
    @GET("/api/Events/GetUnfinished")
    suspend fun loadItems(
        @Query("PageNumber") page: Int,
        @Query("PageSize") pageSize: Int,
        @Query("IsOnline") isOnline: Boolean? = null,
        @Query("Citys") citiesFilters: List<String>? = null,
        @Query("TagsID") tagsFiltersIds: List<Int>? = null,
        @Query("Start") startTime: String? = null,
        @Query("End") endTime: String? = null,
        @Query("IsRegistration") isRegistration: Boolean? = null,
    ): Response<EventsResponse>

    @GET("api/Events/getbyid/{id}")
    suspend fun loadItemById(@Path("id") eventId: String): Response<EventsResponseItem>

    @GET("api/Events/GetMy")
    suspend fun getMyEvents(@Header("Authorization") token: String): Response<EventsResponse>

    @POST("api/Events")
    suspend fun addNewEvent(@Body event: EventAddModel, @Header("Authorization") token: String): Response<String>
}