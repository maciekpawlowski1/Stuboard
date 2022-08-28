package com.pawlowski.stuboard.di

import android.app.Application
import com.pawlowski.stuboard.domain.EventsRepository
import com.pawlowski.stuboard.domain.FakeEventsRepositoryImpl
import com.pawlowski.stuboard.domain.FakePreferencesRepositoryImpl
import com.pawlowski.stuboard.domain.PreferencesRepository
import com.pawlowski.stuboard.presentation.use_cases.GetEventDetailsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetHomeEventTypesSuggestionsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetPreferredCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun applicationContext(application: Application) = application.applicationContext

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
        return FakeEventsRepositoryImpl()
    }

    @Singleton
    @Provides
    fun getPreferredCategoriesUseCase(preferencesRepository: PreferencesRepository) = GetPreferredCategoriesUseCase(preferencesRepository::getCategoriesInPreferredOrder)

    @Singleton
    @Provides
    fun getHomeEventTypesSuggestionsUseCase(eventsRepository: EventsRepository) = GetHomeEventTypesSuggestionsUseCase(eventsRepository::getHomeEventTypesSuggestion)

    @Singleton
    @Provides
    fun getEventDetailsUseCase(eventsRepository: EventsRepository) = GetEventDetailsUseCase(eventsRepository::getEventDetails)
}