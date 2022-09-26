package com.pawlowski.stuboard.presentation.account

import android.net.Uri

data class AccountUiSate(
    val displayName: String,
    val mail: String,
    val profilePhoto: Uri? = null,
    val isAdmin: Boolean? = null,
)
