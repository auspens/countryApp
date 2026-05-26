package com.sumup.countryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.repository.CountryRepository
import com.sumup.countryapp.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CountryDirectoryViewModel @Inject constructor(
    private val repository: CountryRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        retryFetch()
    }

    fun retryFetch() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val response = repository.fetchCountriesAndRegions()
            val favorites = favouritesRepository.getFavouriteCountries()
            when {
                response.isSuccess -> {
                    _uiState.value =
                        HomeUiState.Data(
                            response.getOrDefault(emptyList()).toImmutableList(),
                            repository.regions, "All", favorites, currentScreen = CurrentScreen.All
                        )
                }

                else -> {
                    _uiState.value = HomeUiState.Error
                }
            }
        }
    }

    fun applyFilter(filter: String) {
        if (_uiState.value !is HomeUiState.Data) return
        if (filter == (_uiState.value as HomeUiState.Data).filter) return
        if (filter == "All") {
            if ((_uiState.value as HomeUiState.Data).currentScreen == CurrentScreen.Saved) {
                switchToFavourites()
                return
            }
            _uiState.value = HomeUiState.Data(
                repository.countries,
                repository.regions,
                filter,
                (_uiState.value as HomeUiState.Data).saved,
                currentScreen = CurrentScreen.All
            )
        } else {
            val countries = repository.countries.filter { it.region == filter }.toImmutableList()
            if ((_uiState.value as HomeUiState.Data).currentScreen == CurrentScreen.Saved) {
                _uiState.value = HomeUiState.Data(
                    countries.filter {
                        (_uiState.value as HomeUiState.Data).saved.contains(
                            it.name?.common ?: ""
                        )
                    }.toImmutableList(),
                    repository.regions,
                    filter,
                    (_uiState.value as HomeUiState.Data).saved,
                    currentScreen = CurrentScreen.Saved
                )
                return
            }
            _uiState.value = HomeUiState.Data(
                countries, repository.regions, filter, (_uiState.value as HomeUiState.Data).saved,
                CurrentScreen.All
            )
        }
    }

    fun toggleFavorite(countryName: String) {
        if (_uiState.value !is HomeUiState.Data) return
        val currentState = _uiState.value as HomeUiState.Data
        val saved = currentState.saved.toMutableSet()
        if (saved.contains(countryName)) {
            viewModelScope.launch {
                favouritesRepository.removeFromFavourites(countryName)
                saved.remove(countryName)
                _uiState.value = currentState.copy(saved = saved)
            }
        } else {
            viewModelScope.launch {
                favouritesRepository.addToFavourites(countryName)
                saved.add(countryName)
                _uiState.value = currentState.copy(saved = saved)
            }
        }
    }

    fun switchToFavourites() {
        if (_uiState.value !is HomeUiState.Data) return
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val response: Set<String> = favouritesRepository.getFavouriteCountries()
            _uiState.value = HomeUiState.Data(
                repository.countries.filter { response.contains(it.name?.common ?: "") }
                    .toImmutableList(),
                repository.regions, "Favourites", response, currentScreen = CurrentScreen.Saved
            )
        }
    }

    fun switchToAll() {
        if (_uiState.value !is HomeUiState.Data) return
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val response: Set<String> = favouritesRepository.getFavouriteCountries()
            _uiState.value = HomeUiState.Data(
                repository.countries,
                repository.regions, "All", response, currentScreen = CurrentScreen.All
            )
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(
        val countries: ImmutableList<CountryBasic>,
        val regions: ImmutableList<String>,
        val filter: String,
        val saved: Set<String>,
        val currentScreen: CurrentScreen
    ) : HomeUiState

    data object Error : HomeUiState
}

sealed interface CurrentScreen {
    data object All : CurrentScreen
    data object Saved : CurrentScreen
}