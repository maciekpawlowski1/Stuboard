package com.pawlowski.stuboard.data.local

import com.pawlowski.stuboard.data.ISuggestedFiltersProvider
import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryFiltersDao @Inject constructor(
    private val suggestedFiltersProvider: ISuggestedFiltersProvider,
): IFiltersDao {

    private val selectedFilters: MutableStateFlow<List<FilterModel>> = MutableStateFlow(listOf())
    private val allSuggestedFilters: List<FilterModel> = suggestedFiltersProvider.getSuggestedFilters()

    override suspend fun selectFilter(filterModel: FilterModel) {
        selectedFilters.update {
            it.toMutableList().apply {
                if(!contains(filterModel))
                    add(filterModel)
            }
        }
    }

    override suspend fun unselectFilter(filterModel: FilterModel) {
        selectedFilters.update {
            it.toMutableList().apply {
                remove(filterModel)
            }
        }
    }

    override suspend fun unselectAllFilters() {
        selectedFilters.update { listOf() }
    }

    override fun getSelectedFilters(): Flow<List<FilterModel>> {
        return selectedFilters.asStateFlow()
    }

    override fun getSelectedFiltersCount(): Flow<Int> {
        return selectedFilters.asStateFlow().map { it.size }
    }

    override fun getAllSuggestedNotSelectedFilters(): Flow<List<FilterModel>> {
        return selectedFilters.asStateFlow().map { selected -> allSuggestedFilters.filterNot { selected.contains(it) } }
    }
}