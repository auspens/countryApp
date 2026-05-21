package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryBasic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface CountryRepository {

    val countries:ImmutableList<CountryBasic>
    val regions: ImmutableList<String>

    suspend fun fetchCountriesAndRegions(): Result<List<CountryBasic>>
}
