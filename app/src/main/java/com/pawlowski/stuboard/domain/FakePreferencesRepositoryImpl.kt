package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.ui.models.CategoryItem
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePreferencesRepositoryImpl: PreferencesRepository {
    override fun getCategoriesInPreferredOrder(): Flow<List<CategoryItem>> = flow {
        delay(20)
        emit(PreviewUtils.categoryItemsForPreview)
    }

}