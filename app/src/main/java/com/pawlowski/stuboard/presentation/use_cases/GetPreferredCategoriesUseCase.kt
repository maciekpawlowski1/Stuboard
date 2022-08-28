package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.ui.models.CategoryItem
import kotlinx.coroutines.flow.Flow

fun interface GetPreferredCategoriesUseCase: () -> Flow<List<CategoryItem>>