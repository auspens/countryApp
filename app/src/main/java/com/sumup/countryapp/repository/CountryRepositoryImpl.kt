package com.sumup.countryapp.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.sumup.countryapp.api.CountryAppApi


class CountryRepositoryImpl @Inject constructor(
    val countryAppApi: CountryAppApi
) : CountryRepository {
    private var _countries = mutableStateListOf <CountryBasic>()
    private var _regions = mutableStateListOf("All")

    override val countries: List<CountryBasic>
        get() = _countries

    override val regions: List<String>
        get() = _regions


    override suspend fun fetchCountriesAndRegions(): Result<List<CountryBasic>> {


        return runCatching {
            withContext(Dispatchers.IO) {
                countryAppApi.getCountries("cca2,name,flags,region")
            }
        }
            .mapCatching { response ->
                if (!response.isSuccessful) {
                    throw Exception(response.errorBody()?.string() ?: "Unknown error")
                }
                val countriesResponse = response.body() ?: throw Exception("Response body is null")
                saveCountriesAndRegions(countriesResponse)
                _countries
            }
    }
    private fun saveCountriesAndRegions(countriesResponse: List<CountryBasic>) {
        val setOfRegions = mutableSetOf<String>()
        _countries = countriesResponse
            .filter { countryBasic -> countryBasic.countryCode != null }
            .toMutableStateList()
        _countries.map { country ->
            country.region?.let {
                setOfRegions.add(it)
            }
        }
        _regions.addAll(setOfRegions)
}

override suspend fun fetchCountryDetailsByCode(countryCode: String): Result<CountryFull> {
    runCatching {
        withContext(Dispatchers.IO) {
            countryAppApi.getCountryByCode(countryCode)
        }
    }.onFailure { exception -> return Result.failure(exception) }
        .onSuccess { response ->
            if (response.isSuccessful) {
                val countryResponse = response.body()
                return if (!countryResponse.isNullOrEmpty()) {
                    Result.success(countryResponse[0])
                } else {
                    Result.failure(Exception(response.errorBody().toString()))
                }
            }
        }
    return Result.failure(Exception("Reached the end of runCatching"))
}

    override fun updateFavouriteStatus(countryCode: String, isFavourite: Boolean) {
        val index = _countries.indexOfFirst { it.countryCode == countryCode }
        if (index != -1) {
            _countries[index] = _countries[index].copy(isFavourite = isFavourite)
        }
    }
}