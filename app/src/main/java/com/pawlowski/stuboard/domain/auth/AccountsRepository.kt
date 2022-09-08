package com.pawlowski.stuboard.domain.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.pawlowski.stuboard.data.authentication.AuthenticationResult
import com.pawlowski.stuboard.data.authentication.IAuthManager
import com.pawlowski.stuboard.domain.Response
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AccountsRepository @Inject constructor(
    private val authManager: IAuthManager,
    private val oneTapClient: SignInClient,
    @Named("SIGN_IN")
    private val signInRequest: BeginSignInRequest,
    @Named("SIGN_UP")
    private val signUpRequest: BeginSignInRequest,
): IAccountsRepository {


    override fun getLogInState(): AppLoginState {
        return if(authManager.isSignedIn())
            AppLoginState.LoggedIn
        //TODO: Add if logged anonymously
        else {
            AppLoginState.NotLoggedIn
        }
    }

    override fun observeLogInState(): Flow<AppLoginState> = flow {
        authManager.observeAuthState().collect {
            val user = it.currentUser
            if(user != null)
                emit(AppLoginState.LoggedIn)
            else if(false) //TODO: Add if logged anonymously
                emit(AppLoginState.LoggedInAnonymously)
            else
                emit(AppLoginState.NotLoggedIn)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logInWithEmailAndPassword(email: String, password: String): AuthenticationResult {
        return withContext(Dispatchers.IO) {
            authManager.signInWithPassword(email, password)
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): AuthenticationResult {
        return withContext(Dispatchers.IO) {
            authManager.registerWithPassword(email, password)
        }
    }

    override suspend fun addUsernameToUser(
        user: FirebaseUser,
        username: String
    ): AuthenticationResult {
        return withContext(Dispatchers.IO) {
            authManager.addUsernameToUser(user, username)
        }
    }

    override fun oneTapSignInWithGoogle() = flow {
        try {
            emit(Response.Loading)
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            emit(Response.Success(signInResult))
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                emit(Response.Success(signUpResult))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun firebaseSignInWithGoogle(googleCredential: AuthCredential) = flow {
        try {
            emit(Response.Loading)
            val authResult = authManager.signInWithCredentials(googleCredential).getOrThrow()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                //TODO
            }
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

}