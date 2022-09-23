package com.pawlowski.stuboard.data.mappers

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.data.remote.models.EventAddModel
import com.pawlowski.stuboard.data.remote.models.EventsResponse
import com.pawlowski.stuboard.data.remote.models.EventsResponseItem
import com.pawlowski.stuboard.data.remote.models.Tag
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.edit_event.Organisation
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.ui.models.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun EventsResponse.toEventItemForPreviewList(): List<EventItemForPreview>
{
    return this.map {
        it.toEventItemForPreview()
    }
}

fun dateTimeFormatter(): DateTimeFormatter
{
    return DateTimeFormatter.ofPattern("dd.MM.u HH:mm")
}

fun timeFormatter(): DateTimeFormatter
{
    return DateTimeFormatter.ofPattern("HH:mm")
}

fun dateFormatter(): DateTimeFormatter
{
    return DateTimeFormatter.ofPattern("dd.MM.u")
}

fun OffsetDateTime.isSameDayAs(otherDate: OffsetDateTime): Boolean
{
    return this.dayOfMonth == otherDate.dayOfMonth &&
            this.monthValue == otherDate.monthValue &&
            this.year == otherDate.year
}

fun offsetDateTimeStringToLocalFormattedTimeStringForPreview(
    offsetDateTimeStartDateString: String,
    offsetDateTimeEndDateString: String
): String
{
    return try {
        val offset = OffsetDateTime.now().offset
        val startOffsetTime = OffsetDateTime.parse(offsetDateTimeStartDateString).withOffsetSameInstant(offset)
        val endOffsetTime = OffsetDateTime.parse(offsetDateTimeEndDateString).withOffsetSameInstant(offset)
        if(startOffsetTime.isSameDayAs(endOffsetTime))
        {
            "${startOffsetTime.format(dateTimeFormatter())} - ${endOffsetTime.format(timeFormatter())}"
        }
        else
        {
            "${startOffsetTime.format(dateFormatter())} - ${endOffsetTime.format(dateFormatter())}"
        }
    }
    catch (e: Exception)
    {
        ""
    }
}

fun offsetDateTimeStringToLocalLong(offsetDateTimeString: String?): Long?
{
    return try {
        offsetDateTimeString?.let {
            val offset = OffsetDateTime.now().offset
            val offsetTime = OffsetDateTime.parse(offsetDateTimeString).withOffsetSameInstant(offset)
            Date.from(offsetTime.toInstant()).time
        }
    }
    catch (e: Exception)
    {
        null
    }
}


fun EventsResponseItem.toEventItemForPreview(): EventItemForPreview {
    val dateString = offsetDateTimeStringToLocalFormattedTimeStringForPreview(this.startDate, this.endDate)

    return EventItemForPreview(
        eventId = id,
        tittle = this.name,
        imageUrl = this.thumbnail,
        dateDisplayString = dateString,
        place = if(this.online)
            "Online"
        else
            "${this.city}, ${this.location?:""}",
    )
}

fun EventsResponse.toEventItemForMapScreenList(): List<EventItemForPreviewWithLocation>
{
    return this.mapNotNull {
        it.toEventItemForMapScreen()
    }
}

fun EventsResponse.toFullEventEntitiesList(): List<FullEventEntity> {
    return this.mapNotNull {
        it.toFullEventEntity()
    }
}

fun EventsResponseItem.toFullEventEntity(): FullEventEntity?
{
    val categoryFilters = tags?.mapNotNull {
        it?.id?.let { categoryId ->
            CategoryHandler.getCategoryById(categoryId)
        }
    }?: listOf()

    val registrationFilter = if(registration)
        FilterModel.Registration.RegistrationNeeded
    else
        FilterModel.Registration.NoRegistrationNeeded

    val priceFilter = if(tickets)
        FilterModel.EntryPrice.Paid
    else
        FilterModel.EntryPrice.Free

    val allFilters = listOf(
        categoryFilters,
        listOf(registrationFilter),
        listOf(priceFilter)
    ).flatMap(List<FilterModel>::toList)

    val selectedOrganisation = OrganisationHandler.getOrganisationByTittle(organization)

    return FullEventEntity(
        id = 0,
        tittle = name,
        sinceTime = startDate,
        toTime = endDate,
        isOnline = online,
        city = city,
        streetAndNumber = "",
        placeName = location?:"",
        latitude = latitude,
        longitude = longitude,
        description = shortDescription,
        site = website?:"",
        facebookSite = facebook?:"",
        imageUrl = thumbnail,
        remoteEventId = id,
        publishingStatus = if(published)
            2
        else
            1,
        filtersJson = allFilters.toJson(Gson()),
        organisationId = if(selectedOrganisation is Organisation.Existing)
            selectedOrganisation.id
        else
            -1,
        customOrganisationTittle = if(selectedOrganisation is Organisation.Custom)
            selectedOrganisation.tittle
        else
            null,
    )
}

fun EventItemForPreviewWithLocation.toNotSelectedEventMarker(): EventMarker {
    return EventMarker(
        position = position,
        iconId = mainCategoryDrawableId,
        eventId = eventId,
        eventTittle = tittle
    )
}

fun EventsResponseItem.toEventItemForMapScreen(): EventItemForPreviewWithLocation?
{
    if(latitude == null || longitude == null || (latitude == 0.0 && longitude == 0.0) || online)
        return null

    val category = CategoryHandler.getCategoryById(tags?.getOrNull(0)?.id?:-1)
    return EventItemForPreviewWithLocation(
        eventId = id,
        tittle = name,
        place = "${this.city}, ${this.location?:""}",
        dateDisplayString = offsetDateTimeStringToLocalFormattedTimeStringForPreview(startDate, endDate),
        position = LatLng(latitude, longitude),
        imageUrl = thumbnail,
        mainCategoryDrawableId = category.markerDrawableId,
        mainCategoryDrawableIdWhenSelected = category.selectedMarkerDrawableId,
        isFree = !tickets
    )
}

fun EventsResponseItem.toEventItemWithDetails(): EventItemWithDetails {
    val (dateText, hoursText) = try {
        val localOffset = OffsetDateTime.now().offset
        val offsetDateTimeStartDate = OffsetDateTime.parse(startDate).withOffsetSameInstant(localOffset)
        val offsetDateTimeEndDate = OffsetDateTime.parse(endDate).withOffsetSameInstant(localOffset)
        val (dateText, hoursText) = if(offsetDateTimeStartDate.isSameDayAs(offsetDateTimeEndDate))
        {
            val date = offsetDateTimeStartDate.format(dateFormatter())
            val time = "${offsetDateTimeStartDate.format(timeFormatter())} - ${offsetDateTimeEndDate.format(
                timeFormatter())}"
            Pair(date, time)
        }
        else
            Pair("${offsetDateTimeStartDate.format(dateTimeFormatter())} - ${offsetDateTimeEndDate.format(dateTimeFormatter())}","")

        Pair(dateText, hoursText)
    }
    catch (e: Exception)
    {
        Pair("", "")
    }




    val categories = tags?.mapNotNull {
        it?.id?.let { categoryId ->
            CategoryHandler.getCategoryById(categoryId)
        }
    }?: listOf()

    return EventItemWithDetails(
        tittle = this.name,
        imageUrl = this.thumbnail,
        dateDisplay = dateText,
        hourDisplay = hoursText,
        place = if(this.online)
            "Online"
        else
            "${this.city}, ${this.location?:""}",
        description = this.shortDescription,
        isFree = !tickets,
        categoriesDrawablesId = categories.map { it.iconDrawableId },
        organisation = OrganisationItemForPreview(tittle = organization)
    )
}

fun FullEventEntity.toEventItemForPreview(): EventItemForPreview
{
    return EventItemForPreview(
        eventId = id.toString(),
        tittle = tittle.ifEmpty { "Bez nazwy" },
        place = "$city, ${placeName.ifEmpty { streetAndNumber }}",
        isFree = isFree(),
        imageUrl = imageUrl?:"",
        dateDisplayString = if(sinceTime != null && toTime != null)
            offsetDateTimeStringToLocalFormattedTimeStringForPreview(sinceTime, toTime)
        else
            ""
    )
}

fun FullEventEntity.toEventAddModel(): EventAddModel?
{
    return try {
        val filters = this.filtersJson.toFilterModelList(Gson())
        val groupedFilters = filters?.groupBy { it.filterType }
        val tags = groupedFilters?.get(FilterType.CATEGORY)?.map {
            val category = (it as FilterModel.Category)
            Tag(id = category.categoryId, name = category.tittle)
        }

        val organisation = organisationId?.let {
            OrganisationHandler.getExistingOrganisationById(it)?.tittle
        }?:customOrganisationTittle?:"" //TODO

        EventAddModel(
            name = tittle,
            background = imageUrl,
            thumbnail = imageUrl,
            latitude = latitude,
            longitude = longitude,
            online = isOnline,
            registration = groupedFilters?.get(FilterType.REGISTRATION)?.firstOrNull() == FilterModel.Registration.RegistrationNeeded,
            shortDescription = description,
            startDate = sinceTime!!,
            endDate = toTime!!,
            tags = tags,
            tickets = groupedFilters?.get(FilterType.ENTRY_PRICE)?.firstOrNull() == FilterModel.EntryPrice.Paid,
            website = site,
            facebook = facebookSite,
            language = "Not specified", //TODO: Add selecting language
            city = city,
            location = placeName.ifEmpty { streetAndNumber },
            organization = organisation,
            id = remoteEventId
        )
    }
    catch (e: Exception)
    {
        null
    }

}

fun List<EventItemForPreviewWithLocation>.toEventItemForPreviewList(): List<EventItemForPreview>
{
    return this.map { it.toEventItemForPreview() }
}

fun EventItemForPreviewWithLocation.toEventItemForPreview(): EventItemForPreview
{
    return EventItemForPreview(
        tittle = tittle,
        dateDisplayString = dateDisplayString,
        place = place,
        isFree = isFree,
        imageUrl = imageUrl,
        eventId = eventId
    )
}

fun Int.toPublishingStatus(): EventPublishState
{
    return when(this) {
        0 -> EventPublishState.EDITING
        1 -> EventPublishState.WAITING_TO_PUBLISH
        2 -> EventPublishState.PUBLISHED
        3 -> EventPublishState.CANCELED
        else -> EventPublishState.EDITING
    }
}

fun FullEventEntity.toEditEventUiState(): EditEventUiState
{
    val organisation = organisationId?.let {
        if(it != -1)
        {
            OrganisationHandler.getExistingOrganisationById(it)
        }
        else
            Organisation.Custom(customOrganisationTittle?:"")
    }

    val organisationSearchText = organisationId?.let {
        if(it == -1)
        {
            customOrganisationTittle
        }
        else
            ""
    }?:""

    val suggestedOrganisations = OrganisationHandler.getAllExistingOrganisations()
        .filter {
            organisationSearchText.isEmpty() || it.tittle.contains(organisationSearchText)
        }

    val filters = this.filtersJson.toFilterModelList(Gson())
    val groupedFilters = filters?.groupBy {
        it.filterType
    }
    val categories = EditEventInitialCategories.initialCategories.mapValues {
        it.value
            .toMutableMap()
            .apply {
                groupedFilters?.get(it.key)?.forEach { selectedFilter ->
                    put(selectedFilter, true)
                }
            }
            .toMap()
    }

    val markerDrawableRes = groupedFilters?.get(FilterType.CATEGORY)?.firstOrNull()?.let {
        (it as FilterModel.Category).markerDrawableId
    }



    return EditEventUiState(
        tittleInput = this.tittle,
        eventId = this.id,
        toTime = offsetDateTimeStringToLocalLong(this.toTime),
        sinceTime = offsetDateTimeStringToLocalLong(this.sinceTime),
        city = this.city,
        country = this.country,
        isOnline = this.isOnline,
        streetAndNumber = this.streetAndNumber,
        placeName = this.placeName,
        positionOnMap = if(latitude != null && longitude != null)
            LatLng(this.latitude, this.longitude)
        else
            null,
        markerDrawableRes = markerDrawableRes,
        description = this.description,
        site = this.site,
        facebookSite = this.facebookSite,
        selectedOrganisation = organisation,
        organisationSearchText = organisationSearchText,
        categories = categories,
        suggestedOrganisations = suggestedOrganisations,
        imageUrl = imageUrl,
        publishingStatus = publishingStatus.toPublishingStatus()
    )
}


fun FullEventEntity.isFree(): Boolean
{
    return try {
        val filters = this.filtersJson.toFilterModelList(Gson())
        val groupedFilters = filters?.groupBy {
            it.filterType
        }
        val isFree = groupedFilters?.get(FilterType.ENTRY_PRICE)
            ?.filterIsInstance<FilterModel.EntryPrice>()
            ?.firstOrNull() == FilterModel.EntryPrice.Free

        isFree
    }
    catch (e: Exception)
    {
        false
    }

}

fun EditEventUiState.toFullEventEntity(): FullEventEntity
{

    val selectedFilters = categories.map {
        it.value.filter { filter -> filter.value }
    }.filter {
        it.isNotEmpty()
    }.flatMap {
        it.keys
    }
    val gson = Gson()
    val categoriesJson = selectedFilters.toJson(gson)


    val offset = OffsetDateTime.now().offset
    val startTime = sinceTime?.let {
        Date(it).toInstant().atOffset(offset)
    }.toString()

    val endTime = toTime?.let {
        Date(it).toInstant().atOffset(offset)
    }.toString()

    return FullEventEntity(
        id = eventId,
        tittle = tittleInput,
        sinceTime = startTime,
        toTime = endTime,
        isOnline = isOnline,
        city = city,
        streetAndNumber = streetAndNumber,
        country = country,
        placeName = placeName,
        latitude = positionOnMap?.latitude,
        longitude = positionOnMap?.longitude,
        organisationId = when (selectedOrganisation) {
            null -> null
            is Organisation.Existing -> selectedOrganisation.id
            else -> -1
        },
        customOrganisationTittle = if(selectedOrganisation is Organisation.Custom)
            selectedOrganisation.tittle
        else
            null,
        description = description,
        site = site,
        facebookSite = facebookSite,
        filtersJson = categoriesJson,
        imageUrl = imageUrl,
        publishingStatus = publishingStatus.ordinal
    )
}


private fun String.toFilterModelList(gson: Gson): List<FilterModel>?
{
    return try {
        val listType = object: TypeToken<List<SerializedFilterHolder>>(){}.type
        val serializedHoldersList = gson.fromJson<List<SerializedFilterHolder>>(this, listType)
        serializedHoldersList.mapNotNull {
            it.deserialize()
        }
    }
    catch (e: Exception) {
        null
    }

}

private fun List<FilterModel>.toJson(gson: Gson): String
{
    val jsonStringList = this.map { it.serialize() }
    return gson.toJson(jsonStringList)
}

private data class SerializedFilterHolder(
    val filterTypeId: Int,
    val serializedFilter: String
)

private fun SerializedFilterHolder.deserialize(): FilterModel?
{
    return try {
        return when(this.filterTypeId)
        {
            FilterType.CATEGORY.filterTypeId -> {
                CategoryHandler.getCategoryById(serializedFilter.toInt())
            }
            FilterType.ACCESS.filterTypeId -> {
                if(serializedFilter == "everybody")
                    FilterModel.Access.EVERYBODY
                else
                    FilterModel.Access.PROTECTED
            }
            FilterType.REGISTRATION.filterTypeId -> {
                if(serializedFilter == "needed")
                    FilterModel.Registration.RegistrationNeeded
                else
                    FilterModel.Registration.NoRegistrationNeeded
            }
            FilterType.ENTRY_PRICE.filterTypeId -> {
                if(serializedFilter == "free")
                    FilterModel.EntryPrice.Free
                else
                    FilterModel.EntryPrice.Paid
            }
            FilterType.OTHER.filterTypeId -> {
                if(serializedFilter == "inside")
                    FilterModel.Other.Inside
                else
                    FilterModel.Other.Outside
            }
            else -> null
        }
    }
    catch (e: Exception)
    {
        null
    }

}

private fun FilterModel.serialize(): SerializedFilterHolder
{
    val serializedFilter =  when(this) {
        is FilterModel.Category -> {
            "${this.categoryId}"
        }
        is FilterModel.Registration.RegistrationNeeded -> {
            "needed"
        }
        is FilterModel.Registration.NoRegistrationNeeded -> {
            "notNeeded"
        }
        is FilterModel.Access.EVERYBODY -> {
            "everybody"
        }
        is FilterModel.Access.PROTECTED -> {
            "protected"
        }
        is FilterModel.EntryPrice.Free -> {
            "free"
        }
        is FilterModel.EntryPrice.Paid -> {
            "paid"
        }
        is FilterModel.Other.Inside -> {
            "inside"
        }
        is FilterModel.Other.Outside -> {
            "outside"
        }

        else -> {
            "Unknown filter"
        }
    }

    return SerializedFilterHolder(filterTypeId = this.filterType.filterTypeId, serializedFilter = serializedFilter)
}

