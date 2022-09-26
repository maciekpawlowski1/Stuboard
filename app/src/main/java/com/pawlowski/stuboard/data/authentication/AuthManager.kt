package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.*
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.utils.UiText
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

    override suspend fun signInWithPassword(mail: String, password: String): Resource<FirebaseUser> {

        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(mail, password).await()?.user
            //println("token: ${firebaseAuth.currentUser?.getIdToken(true)?.await()?.token}")

            if(user != null)
                Resource.Success(user)
            else
                Resource.Error(UiText.StaticText("Unknown error"))
        }
        catch (e: Exception)
        {
            Resource.Error(UiText.StaticText(e.localizedMessage?:"Some error happened"))
        }
    }

    override suspend fun registerWithPassword(mail: String, password: String): Resource<FirebaseUser> {
        return try {
            val user = firebaseAuth.createUserWithEmailAndPassword(mail, password).await()?.user
            if(user != null)
                Resource.Success(user)
            else
                Resource.Error(UiText.StaticText("Unknown error"))
        }
        catch (e: Exception)
        {
            Resource.Error(UiText.StaticText(e.localizedMessage?:"Some error happened"))
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


    override suspend fun addUsernameToUser(user: FirebaseUser, username: String): Resource<FirebaseUser> {
        return try {
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build()).await()
            Resource.Success(user)
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(UiText.StaticText(e.localizedMessage?:"Some problem happened"))
        }
    }

    override suspend fun getApiToken(): String? {
        return getToken()?.token
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

    private suspend fun getToken(): GetTokenResult?
    {
        return firebaseAuth.currentUser?.getIdToken(true)?.await()
    }

    override suspend fun getUserRole(): String? {
        return try {
            getToken()!!.claims["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"]?.toString()
        }
        catch (e: Exception)
        {
            null
        }
    }
}