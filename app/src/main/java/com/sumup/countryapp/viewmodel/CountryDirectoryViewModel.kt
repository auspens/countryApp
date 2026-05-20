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
        retryFetch()
    }

    fun retryFetch() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val response = repository.fetchCountriesAndRegions()
            when {
                response.isSuccess -> {
                    _uiState.value =
                        HomeUiState.Data(response.getOrDefault(emptyList()).toImmutableList())
                }
                else -> {
                    _uiState.value = HomeUiState.Error
                }
            }
        }
    }
}


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(val countries: ImmutableList<CountryBasic>) : HomeUiState
    data object Error : HomeUiState
}