package com.pawlowski.stuboard.presentation.account

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.presentation.use_cases.GetCurrentUserUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetIsUserAdminUseCase
import com.pawlowski.stuboard.presentation.use_cases.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getIsUserAdminUseCase: GetIsUserAdminUseCase,
    private val signOutUseCase: SignOutUseCase,
): IAccountViewModel, ViewModel() {

    override val container: Container<AccountUiSate, AccountSingleEvent> = container(AccountUiSate("", ""))


    private fun refreshUserData() = intent {
            val currentUser = getCurrentUserUseCase.invoke()
            currentUser?.let {
                val displayName = it.displayName
                val email = it.email
                val profilePhoto = it.photoUrl
                reduce {
                    state.copy(displayName = displayName?:"", mail= email?:"", profilePhoto = profilePhoto)
                }
            }?: kotlin.run {
                postSideEffect(AccountSingleEvent.NavigateToLogIn)
            }
        }

    override fun myEventsClick() = intent {
        postSideEffect(AccountSingleEvent.NavigateToMyEventsScreen)
    }

    override fun adminPanelClick() = intent {
        postSideEffect(AccountSingleEvent.NavigateToAdminPanelScreen)
    }

    override fun signOut() = intent {
        signOutUseCase()
        postSideEffect(AccountSingleEvent.NavigateToLogIn)
    }

    private fun handleIsUserAdmin() = intent {
        val isAdmin = getIsUserAdminUseCase()
        reduce {
            state.copy(isAdmin = isAdmin)
        }
    }


    init {
        refreshUserData()
        handleIsUserAdmin()
    }
}