package com.pawlowski.stuboard.domain

import android.net.Uri
import android.util.Log
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
import com.pawlowski.stuboard.data.remote.image_storage.IImageUploadManager
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsService: EventsService,
    private val eventsDao: EditingEventsDao,
    private val imageUploadManager: IImageUploadManager,
    private val authManager: IAuthManager
): EventsRepository {
    override fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>> = flow {
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
        emit(firstEmit)
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
                    eventsService = eventsService,
                    filters = filters
                )
            }
        ).flow
    }

    override suspend fun getEventsForMapScreen(filters: List<FilterModel>): Resource<List<EventItemForMapScreen>> {
        return try {
            val result = eventsService.loadItems(1, 100)
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
                    val newEvent = event.copy(imageUrl = downloadUrl)
                    val token = authManager.getApiToken()!!
                    val eventAddModel = newEvent.toEventAddModel()!!
                    val response = eventsService.addNewEvent(eventAddModel, token = "Bearer $token")
                    if(response.isSuccessful)
                    {
                        eventsDao.upsertEvent(newEvent.copy(publishingStatus = 1))
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
}