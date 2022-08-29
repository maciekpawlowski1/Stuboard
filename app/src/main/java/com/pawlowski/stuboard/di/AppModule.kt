package com.pawlowski.stuboard.di

import android.app.Application
import com.pawlowski.stuboard.domain.*
import com.pawlowski.stuboard.presentation.use_cases.*
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
    fun filtersRepository(): IFiltersRepository
    {
        return FakeFiltersRepository()
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

    @Singleton
    @Provides
    fun getSelectedFiltersUseCase(filtersRepository: IFiltersRepository) = GetSelectedFiltersUseCase(filtersRepository::getSelectedFilters)

    @Singleton
    @Provides
    fun getAllSuggestedNotSelectedFilters(filtersRepository: IFiltersRepository) = GetAllSuggestedNotSelectedFiltersUseCase(filtersRepository::getAllSuggestedNotSelectedFilters)
}