package com.sumup.countryapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
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

    private val _detailsUiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val detailsUiState: StateFlow<DetailsUiState> = _detailsUiState

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
                    repository.countries.apply {
                        forEach {
                            it.isFavourite = (favorites.contains(it.name?.common))
                        }
                    }
                    _uiState.value =
                        HomeUiState.Data(
                            response.getOrDefault(SnapshotStateList()),
                            repository.regions, "All", currentScreen = CurrentScreen.All
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
                currentScreen = CurrentScreen.All
            )
        } else {
            val countries = repository.countries.filter { it.region == filter }
            if ((_uiState.value as HomeUiState.Data).currentScreen == CurrentScreen.Saved) {
                _uiState.value = HomeUiState.Data(
                    mutableStateListOf<CountryBasic>().apply {
                        addAll(countries.filter {
                            it.isFavourite
                        })
                    },
                    regions = repository.regions,
                    filter = filter,
                    currentScreen = CurrentScreen.Saved,
                )
                return
            }
            _uiState.value = HomeUiState.Data(
                mutableStateListOf<CountryBasic>().apply { addAll(countries) },
                repository.regions,
                filter,
                CurrentScreen.All
            )
        }
    }

    fun toggleFavorite(countryName: String) {
        if (_uiState.value !is HomeUiState.Data) return
        viewModelScope.launch {
            val savedList = favouritesRepository.getFavouriteCountries().toMutableSet()
            if (savedList.contains(countryName)) {
                favouritesRepository.removeFromFavourites(countryName)
                val index = repository.countries.indexOfFirst { it.name?.common == countryName }
                repository.countries[index] = repository.countries[index].copy(isFavourite = false)
                applyFilter((_uiState.value as HomeUiState.Data).filter)
            } else {
                favouritesRepository.addToFavourites(countryName)
                val index = repository.countries.indexOfFirst { it.name?.common == countryName }
                repository.countries[index] = repository.countries[index].copy(isFavourite = true)
                applyFilter((_uiState.value as HomeUiState.Data).filter)
            }
        }
    }

    fun switchToFavourites() {
        if (_uiState.value !is HomeUiState.Data) return
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = HomeUiState.Data(
                mutableStateListOf<CountryBasic>().apply { addAll(repository.countries.filter { it.isFavourite }) },
                repository.regions, "Favourites", currentScreen = CurrentScreen.Saved
            )
        }
    }

    fun switchToAll() {
        if (_uiState.value !is HomeUiState.Data) return
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = HomeUiState.Data(
                repository.countries,
                repository.regions, "All", currentScreen = CurrentScreen.All
            )
        }
    }

    fun switchToCountryDetails(name: String) {
        viewModelScope.launch {
            _detailsUiState.value = DetailsUiState.Loading
            val response = repository.fetchCountryDetailsByName(name)
            when {
                response.isSuccess -> {
                    val countryInfo = response.getOrNull()
                    if (countryInfo != null) {
                        _detailsUiState.value = DetailsUiState.Data(
                            countryInfo,
                            CurrentScreen.Details
                        )
                    }
                    else {
                        _detailsUiState.value = DetailsUiState.Error
                    }
                }
                else -> {
                    _detailsUiState.value = DetailsUiState.Error
                }
            }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(
        val countries: SnapshotStateList<CountryBasic>,
        val regions: SnapshotStateList<String>,
        val filter: String,
        val currentScreen: CurrentScreen
    ) : HomeUiState

    data object Error : HomeUiState
}

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Data(
        val countryInfo: CountryFull,
        val currentScreen: CurrentScreen
    ) : DetailsUiState
    data object Error : DetailsUiState
}


sealed interface CurrentScreen {
    data object All : CurrentScreen
    data object Saved : CurrentScreen

    data object Details : CurrentScreen
}