package com.pawlowski.stuboard.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.location.Geocoder
import androidx.room.Room
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.data.*
import com.pawlowski.stuboard.data.authentication.AuthManager
import com.pawlowski.stuboard.data.authentication.IAuthManager
import com.pawlowski.stuboard.data.local.IFiltersDao
import com.pawlowski.stuboard.data.local.InMemoryFiltersDao
import com.pawlowski.stuboard.data.local.editing_events.EditingEventsDao
import com.pawlowski.stuboard.data.local.editing_events.EditingEventsDatabase
import com.pawlowski.stuboard.data.remote.EventsService
import com.pawlowski.stuboard.data.remote.image_storage.FirebaseStorageManager
import com.pawlowski.stuboard.data.remote.image_storage.IImageUploadManager
import com.pawlowski.stuboard.domain.*
import com.pawlowski.stuboard.domain.auth.AccountsRepository
import com.pawlowski.stuboard.domain.auth.IAccountsRepository
import com.pawlowski.stuboard.presentation.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun preferencesRepository(preferencesRepository: PreferencesRepository): IPreferencesRepository
    {
        return preferencesRepository
    }

    @Singleton
    @Provides
    fun eventsRepository(eventsRepository: EventsRepositoryImpl): EventsRepository
    {
        return eventsRepository
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

    @Provides
    fun contentResolver(appContext: Context): ContentResolver
    {
        return appContext.contentResolver
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

    @Provides
    fun geocoder(appContext: Context) = Geocoder(appContext)

    @Singleton
    @Provides
    fun editingEventsDatabase(appContext: Context): EditingEventsDatabase {
        return Room.databaseBuilder(appContext, EditingEventsDatabase::class.java, "EditingEventsDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun editingEventsDao(database: EditingEventsDatabase): EditingEventsDao = database.editingEventsDao()

    @Singleton
    @Provides
    fun retrofit(appContext: Context): Retrofit
    {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(appContext.getString(R.string.base_url))
            .build()
    }

    @Singleton
    @Provides
    fun eventsService(retrofit: Retrofit): EventsService = retrofit.create(EventsService::class.java)

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
    fun firebaseStorage(): FirebaseStorage
    {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun imageUploadManager(firebaseStorageManager: FirebaseStorageManager): IImageUploadManager
    {
        return firebaseStorageManager
    }


    @Singleton
    @Provides
    fun getPreferredCategoriesUseCase(preferencesRepository: IPreferencesRepository) = GetPreferredCategoriesUseCase(preferencesRepository::getCategoriesInPreferredOrder)

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

    @Singleton
    @Provides
    fun firebaseSignInWithGoogleUseCase(accountsRepository: IAccountsRepository) = FirebaseSignInWithGoogleUseCase(accountsRepository::firebaseSignInWithGoogle)

    @Singleton
    @Provides
    fun oneTapSignInWithGoogleUseCase(accountsRepository: IAccountsRepository) = OneTapSignInWithGoogleUseCase(accountsRepository::oneTapSignInWithGoogle)

    @Singleton
    @Provides
    fun getCurrentUserUseCase(accountsRepository: IAccountsRepository) = GetCurrentUserUseCase(accountsRepository::getCurrentUser)

    @Singleton
    @Provides
    fun signOutUseCase(accountsRepository: IAccountsRepository) = SignOutUseCase(accountsRepository::signOut)

    @Singleton
    @Provides
    fun getEventsForMapScreenUseCase(eventsRepository: EventsRepository) = GetEventsForMapScreenUseCase(eventsRepository::getEventsForMapScreen)

    @Singleton
    @Provides
    fun getEventPublishingStatusUseCase(eventsRepository: EventsRepository) = GetEventPublishingStatusUseCase(eventsRepository::getEventPublishingStatus)

    @Singleton
    @Provides
    fun saveEditingEventUseCase(eventsRepository: EventsRepository) = SaveEditingEventUseCase(eventsRepository::saveEditingEvent)

    @Singleton
    @Provides
    fun restoreEditEventStateUseCase(eventsRepository: EventsRepository) = RestoreEditEventStateUseCase(eventsRepository::getEditingEventStateFromEditingEvent)

    @Singleton
    @Provides
    fun getAllEditingEventsUseCase(eventsRepository: EventsRepository) = GetAllEditingEventsUseCase(eventsRepository::getAllEditingEvents)

    @Singleton
    @Provides
    fun getEditingEventPreviewUseCase(eventsRepository: EventsRepository) = GetEditingEventPreviewUseCase(eventsRepository::getEditingEventPreview)

    @Singleton
    @Provides
    fun refreshMyEventsUseCase(eventsRepository: EventsRepository) = RefreshMyEventsUseCase(eventsRepository::refreshMyEvents)
}