package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): IAuthManager {


    override fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun signInWithPassword(mail: String, password: String): AuthenticationResult {
        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(mail, password).await()?.user
            if(user != null)
                AuthenticationResult.Success(user)
            else
                AuthenticationResult.Failure(null)
        }
        catch (e: Exception)
        {
            AuthenticationResult.Failure(e.localizedMessage)
        }
    }

    override suspend fun registerWithPassword(mail: String, password: String): AuthenticationResult {
        return try {
            val user = firebaseAuth.createUserWithEmailAndPassword(mail, password).await()?.user
            if(user != null)
                AuthenticationResult.Success(user)
            else
                AuthenticationResult.Failure(null)
        }
        catch (e: Exception)
        {
            AuthenticationResult.Failure(e.localizedMessage)
        }
    }

    override suspend fun signInWithGoogle() {
        TODO("Not yet implemented")
    }

    override suspend fun addUsernameToUser(user: FirebaseUser, username: String): AuthenticationResult {
        return try {
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build()).await()
            AuthenticationResult.Success(user)
        }
        catch (e: Exception) {
            AuthenticationResult.Failure(e.localizedMessage)
        }
    }

    override suspend fun getApiToken(): String? {
        return firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
    }

    override fun observeAuthState(): Flow<FirebaseAuth> = flow {
        val stateChangeFlow = MutableStateFlow(firebaseAuth)
        val listener = FirebaseAuth.AuthStateListener()
        {
            stateChangeFlow.value = it
        }
        try {
            firebaseAuth.addAuthStateListener(listener)
        }
        catch (e: CancellationException)
        {
            firebaseAuth.removeAuthStateListener(listener)
        }
        stateChangeFlow.collect {
            emit(it)
        }
    }


}