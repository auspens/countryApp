package com.sumup.countryapp.api

import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface CountryAppApi{

    @GET("countries/v5")
    suspend fun getCountries(@Query("fields") fields:String): Response<List<CountryBasic>>

    @GET("codes.alpha_2")
    suspend fun getCountryByCode(@Path("cca2") countryCode:String): Response<List<CountryFull>>

}