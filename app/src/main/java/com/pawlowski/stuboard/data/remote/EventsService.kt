package com.pawlowski.stuboard.data.remote

import com.pawlowski.stuboard.ui.models.EventItemForPreview

interface EventsService {
    suspend fun loadItemPreviews(page: Int, pageSize: Int): Result<List<EventItemForPreview>>
}