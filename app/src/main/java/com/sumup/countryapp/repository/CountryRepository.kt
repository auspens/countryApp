package com.sumup.countryapp.repository

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sumup.countryapp.datamodels.CountryBasic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface CountryRepository {

    val countries: SnapshotStateList<CountryBasic>
    val regions: SnapshotStateList<String>

    suspend fun fetchCountriesAndRegions(): Result<SnapshotStateList<CountryBasic>>
}
