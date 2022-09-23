package com.pawlowski.stuboard.domain

import android.net.Uri
import android.webkit.URLUtil
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.pawlowski.stuboard.data.authentication.IAuthManager
import com.pawlowski.stuboard.data.local.editing_events.EditingEventsDao
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.data.mappers.*
import com.pawlowski.stuboard.data.remote.EventsService
import com.pawlowski.stuboard.data.remote.EventsServiceFiltersRequestAdapter
import com.pawlowski.stuboard.data.remote.image_storage.IImageUploadManager
import com.pawlowski.stuboard.data.remote.models.EventsResponse
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.models.EventItemForPreviewWithLocation
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.models.PreviewEventHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsService: EventsService,
    private val eventsDao: EditingEventsDao,
    private val imageUploadManager: IImageUploadManager,
    private val authManager: IAuthManager,
    private val eventsServiceFiltersRequestAdapter: EventsServiceFiltersRequestAdapter,
): EventsRepository {
    override fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>> = channelFlow {
        val firstEmit = listOf(
            HomeEventTypeSuggestion(
                suggestionType = "Najwcześniej",
                isLoading = true,
                events = listOf()
            ),
            HomeEventTypeSuggestion(
                suggestionType = "Online",
                isLoading = true,
                events = listOf(),
                suggestionFilters = listOf(FilterModel.Place.Online),
            )
        )
        send(firstEmit)
        withContext(Dispatchers.IO) {
            val emitsValueState = MutableStateFlow(firstEmit)
            val requests: List<suspend () -> Response<EventsResponse>> = listOf(
                { eventsService.loadItems(1, 10) },
                { eventsService.loadItems(1, 10, isOnline = true) }
            )
            requests.forEachIndexed { index, request ->
                launch {
                    val result = try {
                        request.invoke()
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                        ensureActive()
                        try {
                            //Try again
                            request.invoke()
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    }

                    result?.let { res ->
                        if(res.isSuccessful)
                        {
                            res.body()?.let { body ->
                                val responseEvents = body.map {
                                    if(it.online)
                                        PreviewEventHolder(eventWithoutLocation = it.toEventItemForPreview())
                                    else
                                        PreviewEventHolder(eventWithLocation = it.toEventItemForMapScreen())
                                }
                                emitsValueState.update { prevState ->
                                    prevState.mapIndexed { indexInState, suggestion ->
                                        if(indexInState == index)
                                        {
                                            suggestion.copy(events = responseEvents, isLoading = false)
                                        }
                                        else
                                            suggestion
                                    }
                                }
                            }
                        }
                    }
                }
            }


            emitsValueState.collectLatest {
                send(it)
            }
        }

    }

    override fun getEventDetails(eventId: String): Flow<EventDetailsResult?> = flow {
        try {
           val response = eventsService.loadItemById(eventId)
            emit(EventDetailsResult(isFresh = true,
                event = response.body()!!.toEventItemWithDetails()))
        }
        catch (e: Exception) {

        }
    }

    override fun getEventResultStream(filters: List<FilterModel>): Flow<PagingData<EventItemForPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EventsPagingSourceFactory(
                    eventsService = eventsServiceFiltersRequestAdapter,
                    filters = filters
                )
            }
        ).flow
    }

    override suspend fun getEventsForMapScreen(filters: List<FilterModel>): Resource<List<EventItemForPreviewWithLocation>> {
        return try {
            //println(authManager.getApiToken())

            val result = eventsServiceFiltersRequestAdapter.loadItems(
                page = 1,
                pageSize = 100,
                filters = filters,
            )
            if(result.isSuccessful)
            {
                Resource.Success(result.body()!!.toEventItemForMapScreenList())
            }
            else
                Resource.Error(message = UiText.StaticText(result.message()))
        }
        catch (e: Exception)
        {
            Resource.Error(message = UiText.StaticText(e.localizedMessage?:"Error occurred"))
        }
    }

    override fun getEventPublishingStatus(eventId:Int): Flow<EventPublishState>  {
        return eventsDao.observeEvent(eventId).map { it.publishingStatus.toPublishingStatus() }
    }

    override suspend fun saveEditingEvent(editEventUiState: EditEventUiState): Long {
        return withContext(Dispatchers.IO) {
            eventsDao.upsertEvent(editEventUiState.toFullEventEntity())
        }
    }

    override fun getAllEditingEvents(): Flow<List<FullEventEntity>> {
        return eventsDao.getAllEvents()
    }

    override suspend fun getEditingEventStateFromEditingEvent(eventId: Long): EditEventUiState {
       return eventsDao.getEvent(eventId.toInt()).toEditEventUiState()
    }

    override fun getEditingEventPreview(eventId: Int): Flow<EventItemForPreview> {
        return eventsDao.observeEvent(eventId).map { it.toEventItemForPreview() }
    }

    override suspend fun publishEvent(eventId: Int): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val event = eventsDao.getEvent(eventId)

                val downloadImageResult = if(URLUtil.isFileUrl(event.imageUrl) || URLUtil.isContentUrl(event.imageUrl))
                {
                    imageUploadManager.uploadNewImage(Uri.parse(event.imageUrl), authManager.getCurrentUser()!!.uid)
                }
                else
                {
                    //File already uploaded
                    Resource.Success(event.imageUrl)
                }

                if(downloadImageResult is Resource.Success)
                {
                    val downloadUrl = downloadImageResult.data!!
                    val newEvent = event.copy(imageUrl = downloadUrl, remoteEventId = event.remoteEventId?:UUID.randomUUID().toString())
                    eventsDao.upsertEvent(newEvent)
                    val token = authManager.getApiToken()!!
                    val eventAddModel = newEvent.toEventAddModel()!!
                    println(Gson().toJson(eventAddModel))
                    val response = eventsService.addNewEvent(eventAddModel.copy(), token = "Bearer $token")
                    if(response.isSuccessful)
                    {
                        eventsDao.upsertEvent(newEvent.copy(publishingStatus = 1, remoteEventId = response.body()!!))
                        println("Success of adding event")
                        Resource.Success(true)
                    }
                    else
                    {
                        Resource.Error(UiText.StaticText(response.message()))
                    }
                }
                else
                    Resource.Error(downloadImageResult.message?:UiText.StaticText("Error"))
            } catch (e: Exception)
            {
                e.printStackTrace()
                Resource.Error(UiText.StaticText(e.localizedMessage?:"Error"))
            }
        }

    }

    override suspend fun cancelEvent(eventId: Int): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val event = eventsDao.getEvent(eventId)
                if(event.remoteEventId != null)
                {
                    val result = eventsService.deleteEvent(event.remoteEventId, "Bearer ${authManager.getApiToken()}")
                    if(result.isSuccessful)
                    {
                        eventsDao.upsertEvent(event.copy(publishingStatus = 0))
                        Resource.Success(Unit)
                    }
                    else
                        Resource.Error(UiText.StaticText(result.message()))
                }
                else
                    Resource.Error(UiText.StaticText(""))
            }catch (e: Exception)
            {
                e.printStackTrace()
                Resource.Error(UiText.StaticText(e.localizedMessage?:"Wystąpił błąd"))
            }


        }
    }

    override suspend fun refreshMyEvents(): Resource<Unit> {
        return try {
            val myEventsResult = eventsService.getMyEvents(token = "Bearer ${authManager.getApiToken()!!}")
            if(myEventsResult.isSuccessful)
            {
                val myEvents = myEventsResult.body()!!.toFullEventEntitiesList()
                eventsDao.runTransaction {
                    val currentEvents = eventsDao.getAllEvents().first()
                    val idsMap = currentEvents.associate { Pair(it.remoteEventId, it.id) }
                    val myEventsRemoteKeys = myEvents.map { it.remoteEventId }
                    currentEvents.forEach {
                        if(!myEventsRemoteKeys.contains(it.remoteEventId))
                        {
                            eventsDao.upsertEvent(it.copy(publishingStatus = 0, remoteEventId = null))
                        }
                    }
                    myEvents.forEach {
                        eventsDao.upsertEvent(it.copy(id = idsMap.getOrDefault(it.remoteEventId!!, 0)))
                    }
                }
                Resource.Success(Unit)
            }
            else
            {
                println(myEventsResult.message())
                Resource.Error(UiText.StaticText(myEventsResult.message()))
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(UiText.StaticText(e.localizedMessage?:"Refreshing events error"))
        }
    }
}