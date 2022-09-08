package com.pawlowski.stuboard.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.data.*
import com.pawlowski.stuboard.data.authentication.AuthManager
import com.pawlowski.stuboard.data.authentication.IAuthManager
import com.pawlowski.stuboard.data.remote.FakeEventsService
import com.pawlowski.stuboard.data.local.IFiltersDao
import com.pawlowski.stuboard.data.local.InMemoryFiltersDao
import com.pawlowski.stuboard.domain.*
import com.pawlowski.stuboard.domain.auth.AccountsRepository
import com.pawlowski.stuboard.domain.auth.IAccountsRepository
import com.pawlowski.stuboard.presentation.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun applicationContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun preferencesRepository(): PreferencesRepository
    {
        return FakePreferencesRepositoryImpl()
    }

    @Singleton
    @Provides
    fun eventsRepository(): EventsRepository
    {
        return FakeEventsRepositoryImpl(FakeEventsService())
    }

    @Singleton
    @Provides
    fun filtersRepository(filtersDao: IFiltersDao): IFiltersRepository
    {
        return FiltersRepository(filtersDao)
    }

    @Singleton
    @Provides
    fun suggestedFiltersProvider(defaultSuggestedFiltersProvider: DefaultSuggestedFiltersProvider): ISuggestedFiltersProvider
    {
        return defaultSuggestedFiltersProvider
    }

    @Singleton
    @Provides
    fun filtersDao(suggestedFiltersProvider: ISuggestedFiltersProvider): IFiltersDao
    {
        return InMemoryFiltersDao(suggestedFiltersProvider)
    }

    @Singleton
    @Provides
    fun accountsRepository(accountsRepository: AccountsRepository): IAccountsRepository = accountsRepository

    @Singleton
    @Provides
    fun firebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun authManager(authManager: AuthManager): IAuthManager = authManager

    @Singleton
    @Provides
    fun oneTapClient(appContext: Context) = Identity.getSignInClient(appContext)

    @Named("SIGN_IN")
    @Provides
    fun signInRequest(appContext: Context) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(appContext.getString(R.string.firebase_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build())
        .setAutoSelectEnabled(true)
        .build()

    @Named("SIGN_UP")
    @Provides
    fun signUpRequest(appContext: Context) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(appContext.getString(R.string.firebase_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
        .setAutoSelectEnabled(true)
        .build()


    @Singleton
    @Provides
    fun getPreferredCategoriesUseCase(preferencesRepository: PreferencesRepository) = GetPreferredCategoriesUseCase(preferencesRepository::getCategoriesInPreferredOrder)

    @Singleton
    @Provides
    fun getHomeEventTypesSuggestionsUseCase(eventsRepository: EventsRepository) = GetHomeEventTypesSuggestionsUseCase(eventsRepository::getHomeEventTypesSuggestion)

    @Singleton
    @Provides
    fun getEventDetailsUseCase(eventsRepository: EventsRepository) = GetEventDetailsUseCase(eventsRepository::getEventDetails)

    @Singleton
    @Provides
    fun getSelectedFiltersUseCase(filtersRepository: IFiltersRepository) = GetSelectedFiltersUseCase(filtersRepository::getSelectedFilters)

    @Singleton
    @Provides
    fun getAllSuggestedNotSelectedFilters(filtersRepository: IFiltersRepository) = GetAllSuggestedNotSelectedFiltersUseCase(filtersRepository::getAllSuggestedNotSelectedFilters)

    @Singleton
    @Provides
    fun selectNewFilterUseCase(filtersRepository: IFiltersRepository) = SelectNewFilterUseCase(filtersRepository::selectFilter)

    @Singleton
    @Provides
    fun unselectFilterUseCase(filtersRepository: IFiltersRepository) = UnselectFilterUseCase(filtersRepository::unselectFilter)

    @Singleton
    @Provides
    fun getEventsPagingStreamUseCase(eventsRepository: EventsRepository) = GetEventsPagingStreamUseCase(eventsRepository::getEventResultStream)

    @Singleton
    @Provides
    fun unselectAllFiltersUseCase(filtersRepository: IFiltersRepository) = UnselectAllFiltersUseCase(filtersRepository::unselectAllFilters)

    @Singleton
    @Provides
    fun registerWithEmailAndPasswordUseCase(accountsRepository: IAccountsRepository) = RegisterWithEmailAndPasswordUseCase(accountsRepository::registerWithEmailAndPassword)

    @Singleton
    @Provides
    fun logInWithEmailAndPasswordUseCase(accountsRepository: IAccountsRepository) = LogInWithEmailAndPasswordUseCase(accountsRepository::logInWithEmailAndPassword)

    @Singleton
    @Provides
    fun addUsernameToUserUseCase(accountsRepository: IAccountsRepository) = AddUsernameToUserUseCase(accountsRepository::addUsernameToUser)

    @Singleton
    @Provides
    fun observeLogInStateUseCase(accountsRepository: IAccountsRepository) = ObserveLogInStateUseCase(accountsRepository::observeLogInState)

    @Singleton
    @Provides
    fun getLogInStateUseCase(accountsRepository: IAccountsRepository) = GetLogInStateUseCase(accountsRepository::getLogInState)
}