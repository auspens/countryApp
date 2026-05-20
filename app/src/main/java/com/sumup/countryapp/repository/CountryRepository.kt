package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryBasic
import kotlinx.coroutines.flow.StateFlow

interface CountryRepository {

    val countries:List<CountryBasic>
    val regions: List<String>

    suspend fun fetchCountriesAndRegions(): Result<List<CountryBasic>>
}
