package com.sumup.countryapp.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CountryRepositoryImpl @Inject constructor(
    val countryAppApi: com.sumup.countryapp.api.CountryAppApi
) : CountryRepository {
    private var _countries: SnapshotStateList<CountryBasic> =
        mutableStateListOf()
    private var _regions: SnapshotStateList<String> = mutableStateListOf("All")

    override val countries: SnapshotStateList<CountryBasic>
        get() = _countries

    override val regions: SnapshotStateList<String>
        get() = _regions


    override suspend fun fetchCountriesAndRegions(): Result<SnapshotStateList<CountryBasic>> {
        val setOfRegions = mutableSetOf<String>()

        runCatching {
            withContext(Dispatchers.IO) {
                countryAppApi.getCountries("cca2,name,flags,region")
            }
        }.onFailure { exception -> return Result.failure(exception) }
            .onSuccess { response ->
                if (response.isSuccessful) {
                    val countryResponse = response.body()
                    if (countryResponse != null) {
                        _countries = countryResponse.toMutableStateList()
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

        return Result.failure(Exception("Reached the end of runCatching"))
    }

    override suspend fun fetchCountryDetailsByCode(cca2: String): Result<CountryFull> {
        runCatching {
            withContext(Dispatchers.IO) {
                countryAppApi.getCountryByCode(cca2)
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
}