package com.sumup.countryapp.api

import com.sumup.countryapp.datamodels.CountryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface CountryAppApi{

    @GET("all")
    suspend fun getCountries(@Query("fields") fields:String): Response<List<CountryResponse>>

    @GET("name/{name}")
    suspend fun getCountryByName(@Path("name") countryName:String): Response<List<CountryResponse>>

    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region") regionName:String): Response<List<CountryResponse>>

    @GET("subregion/{subregion}")
    suspend fun getCountriesBySubRegion(@Path("subregion") subRegionName:String): Response<List<CountryResponse>>

    @GET("capital/{capital}")
    suspend fun getCountriesByCapital(@Path("capital") capitalName:String): Response<List<CountryResponse>>

    @GET("currency/{currency}")
    suspend fun getCountriesByCurrency(@Path("currency") currencyName:String): Response<List<CountryResponse>>

    @GET("lang/{language}")
    suspend fun getCountriesByLanguage(@Path("language") languageName:String): Response<List<CountryResponse>>

    @GET("alpha/{code}")
    suspend fun getCountryByCode(@Path("code") countryCode:String): Response <List<CountryResponse>>

    @GET("independent")
    suspend fun getCountriesByIndependence(@Query("status") independenceStatus:Boolean): Response<List<CountryResponse>>
}