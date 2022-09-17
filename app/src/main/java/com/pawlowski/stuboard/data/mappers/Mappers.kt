package com.pawlowski.stuboard.data.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.edit_event.Organisation
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType

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




    return FullEventEntity(
        tittle = tittleInput,
        sinceTime = sinceTime,
        toTime = toTime,
        isOnline = isOnline,
        city = city,
        streetAndNumber = streetAndNumber,
        country = country,
        placeName = placeName,
        latitude = positionOnMap?.latitude,
        longitude = positionOnMap?.longitude,
        organisationId = if(selectedOrganisation is Organisation.Existing)
            selectedOrganisation.id
        else
            -1,
        customOrganisationTittle = if(selectedOrganisation is Organisation.Custom)
            selectedOrganisation.tittle
        else
            null,
        description = description,
        site = site,
        facebookSite = facebookSite,
        filtersJson = categoriesJson
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

