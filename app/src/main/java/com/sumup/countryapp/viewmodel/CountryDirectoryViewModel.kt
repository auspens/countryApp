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
                            repository.regions, "All", favorites
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
            _uiState.value = HomeUiState.Data(
                repository.countries,
                repository.regions, filter, (_uiState.value as HomeUiState.Data).favorites
            )
        } else {
            _uiState.value = HomeUiState.Data(
                repository.countries.filter { it.region == filter }.toImmutableList(),
                repository.regions, filter, (_uiState.value as HomeUiState.Data).favorites
            )
        }
    }

    fun toggleFavorite(countryName: String) {
        if (_uiState.value !is HomeUiState.Data) return
        val currentState = _uiState.value as HomeUiState.Data
        val favorites = currentState.favorites.toMutableSet()
        if (favorites.contains(countryName)) {
            viewModelScope.launch {
                favouritesRepository.removeFromFavourites(countryName)
                favorites.remove(countryName)
                _uiState.value = currentState.copy(favorites = favorites)
            }
        } else {
            viewModelScope.launch {
                favouritesRepository.addToFavourites(countryName)
                favorites.add(countryName)
                _uiState.value = currentState.copy(favorites = favorites)
            }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(
        val countries: ImmutableList<CountryBasic>,
        val regions: ImmutableList<String>,
        val filter: String,
        val favorites: Set<String>
    ) : HomeUiState

    data object Error : HomeUiState
}