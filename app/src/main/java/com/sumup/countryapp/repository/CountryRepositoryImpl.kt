package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryResponse


class CountryRepositoryImpl(
    val countryAppApi: com.sumup.countryapp.api.CountryAppApi
) : CountryRepository {
    override suspend fun getCountriesByRegion(regionName: String):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesBySubRegion(subRegionName: String):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByCapital(capitalName: String):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByCurrency(currencyName: String):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByLanguage(languageName: String):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountryByCode(countryCode: String):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountriesByIndependence(independenceStatus: Boolean):Result<List<CountryResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountryByName(countryName: String) : Result<CountryResponse>{
        try {
            val response = countryAppApi.getCountryByName(countryName)
            if (response.isSuccessful) {
                val countryResponse = response.body()
                return if (countryResponse != null) {
                    Result.success(countryResponse[0])
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                return Result.failure(Exception("Error fetching country: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}