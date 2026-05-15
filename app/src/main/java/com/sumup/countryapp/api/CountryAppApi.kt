package com.sumup.countryapp.api

import com.sumup.countryapp.datamodels.CountryResponse
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import javax.inject.Inject

interface CountryAppApi{

    @GET("all")
    suspend fun getCountries(@Query("fields") fields:String): retrofit2.Response<List<CountryResponse>>

    @GET("name/{name}")
    suspend fun getCountryByName(@Path("name") countryName:String): retrofit2.Response<List<CountryResponse>>

    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region") regionName:String): retrofit2.Response<List<CountryResponse>>

    @GET("subregion/{subregion}")
    suspend fun getCountriesBySubRegion(@Path("subregion") subRegionName:String): retrofit2.Response<List<CountryResponse>>

    @GET("capital/{capital}")
    suspend fun getCountriesByCapital(@Path("capital") capitalName:String): retrofit2.Response<List<CountryResponse>>

    @GET("currency/{currency}")
    suspend fun getCountriesByCurrency(@Path("currency") currencyName:String): retrofit2.Response<List<CountryResponse>>

    @GET("lang/{language}")
    suspend fun getCountriesByLanguage(@Path("language") languageName:String): retrofit2.Response<List<CountryResponse>>

    @GET("alpha/{code}")
    suspend fun getCountryByCode(@Path("code") countryCode:String): retrofit2.Response <List<CountryResponse>>

    @GET("independent")
    suspend fun getCountriesByIndependence(@Query("status") independenceStatus:Boolean): retrofit2.Response<List<CountryResponse>>
}