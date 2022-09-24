package com.pawlowski.stuboard.presentation.account

import org.orbitmvi.orbit.ContainerHost

interface IAccountViewModel: ContainerHost<AccountUiSate, AccountSingleEvent> {
    fun signOut()
    fun myEventsClick()
}