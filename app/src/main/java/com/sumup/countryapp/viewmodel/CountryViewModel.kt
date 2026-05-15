package com.sumup.countryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumup.countryapp.datamodels.CountryResponse
import com.sumup.countryapp.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CountryViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {
    private var country: CountryResponse? = null

    fun getCountryByName(countryName: String) {
        viewModelScope.launch{
            country = repository.getCountryByName(countryName).getOrNull()?.get(0)
        }
    }
    fun getCountryName(): String? {
        return country?.name?.common
    }
    fun getCountryCapital(): String? {
        return country?.capital?.firstOrNull()
    }
    fun getCountryPopulation(): Long? {
        return country?.population
    }
    fun getCountryRegion(): String? {
        return country?.region
    }
    fun getCountrySubRegion(): String? {
        return country?.subregion
    }
    fun getCountryFlagUrl(): String? {
        return country?.flags?.png
    }
    fun getCountryLanguages(): List<String>? {
        return country?.languages?.values?.toList()
    }
    fun getCountryCurrencies(): List<String?>? {
        return country?.currencies?.values?.map { it.name }?.toList()
    }
    fun getCountryGoogleMapsUrl(): String? {
        return country?.maps?.googleMaps
    }
}