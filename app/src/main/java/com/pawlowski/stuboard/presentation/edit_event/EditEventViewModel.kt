package com.pawlowski.stuboard.presentation.edit_event

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(

): IEditEventViewModel, ViewModel() {
    override val container: Container<EditEventUiState, EditEventSingleEvent> = container(
        EditEventUiState(categories = initialCategories())
    )

    override fun moveToNextPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.FIRST -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.THIRD }
                else -> {state.currentPage}
            })
        }
    }

    override fun moveToPreviousPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.THIRD -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.FIRST }
                else -> {state.currentPage}
            })
        }
    }

    override fun changeTittleInput(newValue: String) = intent {
        reduce {
            state.copy(tittleInput = newValue)
        }
    }

    override fun changeSinceTime(newTime: Long) = intent {
        reduce {
            state.copy(sinceTime = newTime)
        }
    }

    override fun changeToTime(newTime: Long) = intent {
        reduce {
            state.copy(toTime = newTime)
        }
    }

    override fun changeIsOnline(isOnline: Boolean) = intent {
        reduce {
            state.copy(isOnline= isOnline)
        }
    }

    override fun changeCategorySelection(category: FilterModel, isSelected: Boolean) = intent {
        reduce {
            state.copy(categories = state.categories.toMutableMap().apply {
                val newMap = get(category.filterType)?.toMutableMap()?.apply {
                    set(category, isSelected)
                }
                set(category.filterType, newMap?: mapOf())
            })
        }
    }

    private fun initialCategories(): Map<FilterType, Map<FilterModel, Boolean>>
    {
        return mapOf(
            Pair(FilterType.CATEGORY, mapOf(
                Pair(FilterModel.Category("Koncerty", R.drawable.guitar_icon), false),
            )),
            Pair(FilterType.ACCESS, mapOf(
                Pair(FilterModel.Access.EVERYBODY, false),
                Pair(FilterModel.Access.PROTECTED, false)
            )),
            Pair(FilterType.REGISTRATION, mapOf(
                Pair(FilterModel.Registration.NoRegistrationNeeded, false),
                Pair(FilterModel.Registration.RegistrationNeeded, false)
            )),
            Pair(FilterType.ENTRY_PRICE, mapOf(
                Pair(FilterModel.EntryPrice.Free, false),
                Pair(FilterModel.EntryPrice.Paid,false)
            )),
            Pair(FilterType.OTHER, mapOf(
                Pair(FilterModel.Other.Outside, false),
                Pair(FilterModel.Other.Inside, false)
            ))

        )

    }

}
