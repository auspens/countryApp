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
    private var _regions: List<String> = emptyList()

    override val countries: List<CountryBasic>
        get() = _countries

    override val regions: List<String>
        get() = _regions


    override suspend fun fetchCountriesAndRegions(): Result<List<CountryBasic>> {
        val setOfRegions = mutableSetOf<String>()

        runCatching {
            withContext(Dispatchers.IO) {
                countryAppApi.getCountries("name,flags,region")
            }
        }.onFailure { exception -> return Result.failure(exception) }
            .onSuccess { response ->
                if (response.isSuccessful) {
                    val countryResponse = response.body()
                    if (countryResponse != null) {
                        _countries = countryResponse
                        countryResponse.map { country ->
                            country.region?.let { setOfRegions.add(it) }
                            _regions = setOfRegions.toList()
                            return Result.success(_countries)
                        }
                    } else {
                        return Result.failure(Exception(response.errorBody().toString()))
                    }
                }
            }

        return Result.failure(Exception("Reached the end of runcatching"))
    }
}