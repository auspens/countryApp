package com.sumup.countryapp.repository

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CountryRepository {

    val countries: List<CountryBasic>
    val regions: List<String>

    suspend fun fetchCountriesAndRegions(): Result<List<CountryBasic>>

    suspend fun fetchCountryDetailsByCode(countryCode: String): Result<CountryFull>

    fun updateFavouriteStatus(countryCode: String, isFavourite: Boolean)
}
