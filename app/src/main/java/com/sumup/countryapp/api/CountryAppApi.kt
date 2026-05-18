package com.sumup.countryapp.api

import com.sumup.countryapp.datamodels.CountryDTOShort
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface CountryAppApi{

    @GET("all")
    suspend fun getCountries(@Query("fields") fields:String): Response<List<CountryDTOShort>>

    @GET("name/{name}")
    suspend fun getCountryByName(@Path("name") countryName:String): Response<List<CountryDTOShort>>

    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region") regionName:String): Response<List<CountryDTOShort>>

    @GET("subregion/{subregion}")
    suspend fun getCountriesBySubRegion(@Path("subregion") subRegionName:String): Response<List<CountryDTOShort>>

    @GET("capital/{capital}")
    suspend fun getCountriesByCapital(@Path("capital") capitalName:String): Response<List<CountryDTOShort>>

    @GET("currency/{currency}")
    suspend fun getCountriesByCurrency(@Path("currency") currencyName:String): Response<List<CountryDTOShort>>

    @GET("lang/{language}")
    suspend fun getCountriesByLanguage(@Path("language") languageName:String): Response<List<CountryDTOShort>>

    @GET("alpha/{code}")
    suspend fun getCountryByCode(@Path("code") countryCode:String): Response <List<CountryDTOShort>>

    @GET("independent")
    suspend fun getCountriesByIndependence(@Query("status") independenceStatus:Boolean): Response<List<CountryDTOShort>>
}