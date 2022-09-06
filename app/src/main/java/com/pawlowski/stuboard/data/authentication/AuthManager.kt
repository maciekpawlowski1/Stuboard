package com.pawlowski.stuboard.data.authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): IAuthManager {


    override suspend fun isSignedIn(): Boolean {
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

    override suspend fun getApiToken(): String? {
        return firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
    }
}