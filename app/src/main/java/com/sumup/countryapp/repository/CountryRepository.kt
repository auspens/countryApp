package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryResponse

interface CountryRepository {
    suspend fun getCountriesByRegion(regionName: String)
    suspend fun getCountriesBySubRegion(subRegionName: String)
    suspend fun getCountriesByCapital(capitalName: String)
    suspend fun getCountriesByLanguage(languageName: String)
    suspend fun getCountryByCode(countryCode: String)
    suspend fun getCountriesByCurrency(currencyName: String)
    suspend fun getCountriesByIndependence(independenceStatus: Boolean)
    suspend fun getCountryByName(countryName: String): Result<List<CountryResponse>>
}