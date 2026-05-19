package com.sumup.countryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumup.countryapp.datamodels.CountryDTOShort
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
class CountryViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {
    val countries: StateFlow<List<CountryDTOShort>> = repository.countries
    val regions: StateFlow<List<String>> = repository.regions
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState
    private val _filteredCountries = MutableStateFlow<List<CountryDTOShort>>(emptyList())
    val filteredCountries: StateFlow<List<CountryDTOShort>> = countries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.initCountries(listOf("name", "region", "flags"))
            repository.initRegions()
        }
        viewModelScope.launch {
            repository.countries.collect{
                countries-> _uiState.value =
                if (countries.isNotEmpty()) HomeUiState.Data(countries.toImmutableList())
                else HomeUiState.Loading
            }
        }
    }

    fun filterByRegion(region: String) {
        _filteredCountries.value = countries.value.filter { country -> country.region == region}
    }

    fun removeFilter() {
        _filteredCountries.value = countries.value
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(val countries: ImmutableList<CountryDTOShort>) : HomeUiState
    data object Error : HomeUiState
}