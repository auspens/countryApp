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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context


@HiltViewModel
class CountryDirectoryViewModel @Inject constructor(
    private val repository: CountryRepository,
    private val favouritesRepository: FavouritesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _detailsUiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val detailsUiState: StateFlow<DetailsUiState> = _detailsUiState

    init {
        fetchCountriesAndRegions()
    }

    fun fetchCountriesAndRegions() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val response = repository.fetchCountriesAndRegions()
            val favorites = favouritesRepository.getFavouriteCountries()
            when {
                response.isSuccess -> {
                    repository.countries.apply {
                        forEach {
                            it.isFavourite = (favorites.contains(it.countryCode))
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

    fun toggleFavorite(countryCode: String) {
        if (_uiState.value !is HomeUiState.Data) return
        viewModelScope.launch {
            val savedList = favouritesRepository.getFavouriteCountries().toMutableSet()
            if (savedList.contains(countryCode)) {
                favouritesRepository.removeFromFavourites(countryCode)
                val index = repository.countries.indexOfFirst { it.countryCode == countryCode }
                repository.countries[index] = repository.countries[index].copy(isFavourite = false)
                applyFilter((_uiState.value as HomeUiState.Data).filter)
            } else {
                favouritesRepository.addToFavourites(countryCode)
                val index = repository.countries.indexOfFirst { it.countryCode == countryCode }
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

    fun switchToCountryDetails(countryCode: String) {
        viewModelScope.launch {
            _detailsUiState.value = DetailsUiState.Loading
            val response = repository.fetchCountryDetailsByCode(countryCode)
            when {
                response.isSuccess -> {
                    val countryInfo = response.getOrNull()
                    if (countryInfo != null) {
                        val savedList = favouritesRepository.getFavouriteCountries().toMutableSet()
                        if (savedList.contains(countryInfo.countryCode)) {
                            countryInfo.isFavourite = true
                        }
                        _detailsUiState.value = DetailsUiState.Data(
                            countryInfo,
                            CurrentScreen.Details
                        )
                    } else {
                        _detailsUiState.value = DetailsUiState.Error
                    }
                }
                else -> {
                    _detailsUiState.value = DetailsUiState.Error
                }
            }
        }

    }

    fun toggleFavoriteInDetails(countryCode: String) {
        if (_detailsUiState.value !is DetailsUiState.Data) return
        viewModelScope.launch {
            toggleFavorite(countryCode)
            val currentDetails = _detailsUiState.value as DetailsUiState.Data
            _detailsUiState.value = DetailsUiState.Data(
                currentDetails.countryInfo.copy(isFavourite = !currentDetails.countryInfo.isFavourite),
                currentDetails.currentScreen
            )
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