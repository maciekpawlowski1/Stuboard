package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.*
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

    override suspend fun signInWithCredentials(authCredential: AuthCredential): Result<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithCredential(authCredential).await()
            Result.success(result)
        }
        catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }


    override suspend fun addUsernameToUser(user: FirebaseUser, username: String): AuthenticationResult {
        return try {
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build()).await()
            AuthenticationResult.Success(user)
        }
        catch (e: Exception) {
            e.printStackTrace()
            AuthenticationResult.Failure(e.localizedMessage)
        }
    }

    override suspend fun getApiToken(): String? {
        return firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun observeAuthState(): Flow<FirebaseAuth> = flow {
        val stateChangeFlow = MutableStateFlow(firebaseAuth)
        val listener = FirebaseAuth.AuthStateListener()
        {
            stateChangeFlow.value = it
        }
        try {
            firebaseAuth.addAuthStateListener(listener)
            stateChangeFlow.collect {
                emit(it)
            }
        }
        catch (e: CancellationException)
        {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }


}