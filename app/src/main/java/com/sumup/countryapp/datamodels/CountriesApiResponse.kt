package com.sumup.countryapp.datamodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountriesListApiResponse(
    val data: CountriesListData? = null,
    val errors: List<ApiError>? = null,
)

@JsonClass(generateAdapter = true)
data class CountriesListData(
    val objects: List<CountryBasic>? = null,
    val meta: ResponseMeta? = null,
)

@JsonClass(generateAdapter = true)
data class CountryDetailApiResponse(
    val data: CountryDetailData? = null,
    val errors: List<ApiError>? = null,
)

@JsonClass(generateAdapter = true)
data class CountryDetailData(
    val objects: List<CountryFull>? = null,
    val meta: ResponseMeta? = null,
)

@JsonClass(generateAdapter = true)
data class ResponseMeta(
    val total: Int? = null,
    val count: Int? = null,
    val limit: Int? = null,
    val offset: Int? = null,
    val more: Boolean? = null,
)

@JsonClass(generateAdapter = true)
data class ApiError(
    val message: String? = null,
)
