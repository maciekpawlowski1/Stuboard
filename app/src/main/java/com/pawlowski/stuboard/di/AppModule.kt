package com.pawlowski.stuboard.di

import android.app.Application
import com.pawlowski.stuboard.data.*
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
}