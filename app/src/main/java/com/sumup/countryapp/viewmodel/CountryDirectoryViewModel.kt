package com.sumup.countryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.repository.CountryRepository
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
    private val repository: CountryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.fetchCountriesAndRegions(listOf("name", "region", "flags"))
                val fetchedCountries = repository.countries
                _uiState.value =
                    if (fetchedCountries.isNotEmpty()) HomeUiState.Data(fetchedCountries.toImmutableList())
                    else HomeUiState.Error
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error
            }
        }
    }

    fun retryFetch(){
        viewModelScope.launch {
            try {
                _uiState.value = HomeUiState.Loading
                repository.fetchCountriesAndRegions(listOf("name", "region", "flags"))
                val fetchedCountries = repository.countries
                _uiState.value =
                    if (fetchedCountries.isNotEmpty()) HomeUiState.Data(fetchedCountries.toImmutableList())
                    else HomeUiState.Error
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error
            }
        }
    }
}


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(val countries: ImmutableList<CountryBasic>) : HomeUiState
    data object Error : HomeUiState
}