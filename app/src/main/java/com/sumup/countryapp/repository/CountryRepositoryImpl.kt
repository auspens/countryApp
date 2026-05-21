package com.sumup.countryapp.repository

import android.util.Log
import com.sumup.countryapp.datamodels.CountryBasic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
    private var _countries: ImmutableList<CountryBasic> =
        emptyList<CountryBasic>().toImmutableList()
    private var _regions: MutableList<String> = mutableListOf("All")

    override val countries: ImmutableList<CountryBasic>
        get() = _countries

    override val regions: ImmutableList<String>
        get() = _regions.toImmutableList()


    override suspend fun fetchCountriesAndRegions(): Result<ImmutableList<CountryBasic>> {
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
                        _countries = countryResponse.toImmutableList()
                        _countries.map { country ->
                            country.region?.let {
                                setOfRegions.add(it)
                            }
                        }
                        _regions.addAll(setOfRegions)
                        return Result.success(_countries)
                    } else {
                        return Result.failure(Exception(response.errorBody().toString()))
                    }
                }
            }

        return Result.failure(Exception("Reached the end of runcatching"))
    }
}