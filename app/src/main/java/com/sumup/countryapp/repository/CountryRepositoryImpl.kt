package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryBasic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.String
import kotlin.collections.mutableSetOf


class CountryRepositoryImpl(
    val countryAppApi: com.sumup.countryapp.api.CountryAppApi
) : CountryRepository {
    private var _countries: List<CountryBasic> = emptyList()
    private var _regions: List<String> =emptyList()

    override val countries: List<CountryBasic>
        get() = _countries

    override val regions: List<String>
        get() = _regions


    override suspend fun fetchCountriesAndRegions(fields: List<String>) {
        val setOfRegions = mutableSetOf<String>()
        val response = withContext(Dispatchers.IO) {
            countryAppApi.getCountries(fields.joinToString(","))
        }
        if (response.isSuccessful) {
            val countryResponse = response.body()
            if (countryResponse != null) {
                _countries = countryResponse
                countryResponse.mapNotNull {
                    country -> country.region?.let{setOfRegions.add(it)}
                }
            } else {
                throw (Exception("Empty response body"))
            }
        } else {
            throw (Exception("Error fetching countries: ${response.code()} ${response.message()}"))
        }
        _regions =  setOfRegions.toList()
    }
}