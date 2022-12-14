package com.pawlowski.stuboard.data.mappers

import com.pawlowski.stuboard.presentation.edit_event.Organisation

object OrganisationHandler {
    fun getExistingOrganisationById(organisationId: Int): Organisation.Existing?
    {
        return when(organisationId) {
            1 -> Organisation.Existing(
                id = 1,
                tittle = "Akademia Górniczo-Hutnicza",
                imageUrl = null
            )
            2 -> Organisation.Existing(
                id = 2,
                tittle = "Klub Studio",
                imageUrl = null
            )
            else -> null
        }
    }

    fun getOrganisationByTittle(tittle: String): Organisation
    {
        println("Organisation tittle: $tittle")
        val existing = getAllExistingOrganisations()
        val existingTittles = existing.map { it.tittle }
        val indexOfTittle = existingTittles.indexOf(tittle)
        return if(indexOfTittle != -1)
        {
            println("found existing: ${existing[indexOfTittle]}")
            existing[indexOfTittle]
        }
        else
        {
            println("not found existing: ${Organisation.Custom(tittle)}")
            Organisation.Custom(tittle)
        }
    }

    fun getAllExistingOrganisations(): List<Organisation.Existing>
    {
        return listOf(
            getExistingOrganisationById(1)!!,
            getExistingOrganisationById(2)!!,
        )
    }
}

