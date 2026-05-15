package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryResponse


class CountryRepositoryImpl(
    val countryAppApi: com.sumup.countryapp.api.CountryAppApi
) : CountryRepository {
    override suspend fun getCountriesByRegion(regionName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesBySubRegion(subRegionName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByCapital(capitalName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByCurrency(currencyName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByLanguage(languageName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountryByCode(countryCode: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByIndependence(independenceStatus: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountryByName(countryName: String) : Result<List<CountryResponse>>{
        val response = countryAppApi.getCountryByName(countryName)
        if(response.isSuccessful){
            val countryResponse = response.body()
            return if(countryResponse != null){
                Result.success(countryResponse)
            } else {
                Result.failure(Exception("Empty response body"))
            }
        } else {
            return Result.failure(Exception("Error fetching country: ${response.code()} ${response.message()}"))
        }
    }
}