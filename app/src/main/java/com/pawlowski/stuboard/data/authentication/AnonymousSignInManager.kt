package com.pawlowski.stuboard.data.authentication

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnonymousSignInManager @Inject constructor(
    private val sharedPreference: SharedPreferences
){

    fun signOutAnonymouslyIfAlreadySignedIn()
    {
        sharedPreference.edit().putBoolean(ANONYMOUS_SIGN_IN_FIELD, false).commit()
    }

    fun isSignedInAnonymously(): Boolean
    {
        return sharedPreference.getBoolean(ANONYMOUS_SIGN_IN_FIELD, false)
    }

    fun signInAnonymously()
    {
        sharedPreference.edit().putBoolean(ANONYMOUS_SIGN_IN_FIELD, true).apply()
    }

    companion object {
        const val ANONYMOUS_SIGN_IN_FIELD = "anonymousSignIn"
    }
}