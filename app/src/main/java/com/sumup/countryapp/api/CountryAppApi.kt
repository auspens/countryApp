package com.sumup.countryapp.api

import com.sumup.countryapp.datamodels.CountriesListApiResponse
import com.sumup.countryapp.datamodels.CountryDetailApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface CountryAppApi {

    @GET("countries/v5")
    suspend fun getCountries(
        @Query("response_fields") fields: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<CountriesListApiResponse>

    @GET("countries/v5/codes.alpha_2/{code}")
    suspend fun getCountryByCode(
        @Path("code") countryCode: String,
    ): Response<CountryDetailApiResponse>
}