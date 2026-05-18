package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryDTOShort
import kotlinx.coroutines.flow.StateFlow

interface CountryRepository {

    val countries:StateFlow<List<CountryDTOShort>>
    val regions: StateFlow<List<String>>

    suspend fun initCountries(fields: List<String>)
    fun initRegions()
}