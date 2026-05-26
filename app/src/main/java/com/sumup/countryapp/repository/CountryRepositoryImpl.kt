package com.sumup.countryapp.repository

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.sumup.countryapp.datamodels.CountryBasic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.String
import kotlin.collections.mutableSetOf


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
                countryAppApi.getCountries("name,flags,region")
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

        return Result.failure(Exception("Reached the end of runcatching"))
    }
}