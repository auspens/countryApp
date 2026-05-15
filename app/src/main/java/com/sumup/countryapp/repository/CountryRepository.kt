package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryResponse

interface CountryRepository {
    suspend fun getCountriesByRegion(regionName: String): Result<List<CountryResponse>>
    suspend fun getCountriesBySubRegion(subRegionName: String):Result<List<CountryResponse>>
    suspend fun getCountriesByCapital(capitalName: String):Result<List<CountryResponse>>
    suspend fun getCountriesByLanguage(languageName: String):Result<List<CountryResponse>>
    suspend fun getCountryByCode(countryCode: String):Result<List<CountryResponse>>
    suspend fun getCountriesByCurrency(currencyName: String):Result<List<CountryResponse>>
    suspend fun getCountriesByIndependence(independenceStatus: Boolean):Result<List<CountryResponse>>
    suspend fun getCountryByName(countryName: String):Result<CountryResponse>
}