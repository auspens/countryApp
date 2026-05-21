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
    private val _regions: MutableList<String> = mutableListOf("All")


    init {
        retryFetch()
    }

    fun retryFetch() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val response = repository.fetchCountriesAndRegions()
            when {
                response.isSuccess -> {
                    _regions.addAll(repository.regions)
                    _uiState.value =
                        HomeUiState.Data(
                            response.getOrDefault(emptyList()).toImmutableList(),
                            _regions.toImmutableList(), "All"
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
                repository.countries.toImmutableList(),
                _regions.toImmutableList(), filter
            )
        } else {
            _uiState.value = HomeUiState.Data(
                repository.countries.filter { it.region == filter }.toImmutableList(),
                _regions.toImmutableList(), filter
            )
        }
    }
}


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Data(
        val countries: ImmutableList<CountryBasic>,
        val regions: ImmutableList<String>,
        val filter: String
    ) : HomeUiState

    data object Error : HomeUiState
}