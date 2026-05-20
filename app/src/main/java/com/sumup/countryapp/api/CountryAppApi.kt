package com.sumup.countryapp.api

import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface CountryAppApi{

    @GET("all")
    suspend fun getCountries(@Query("fields") fields:String): Response<List<CountryBasic>>

    @GET("name/{name}")
    suspend fun getCountryByName(@Path("name") countryName:String): Response<List<CountryFull>>

}