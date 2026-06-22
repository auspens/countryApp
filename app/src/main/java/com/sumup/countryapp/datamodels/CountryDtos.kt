package com.sumup.countryapp.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NamesDto(
    val common: String? = null,
    val official: String? = null,
    @Json(name = "native")
    val nativeName: Map<String, NativeNameDto>? = null,
)

@JsonClass(generateAdapter = true)
data class NativeNameDto(
    val official: String? = null,
    val common: String? = null,
)

@JsonClass(generateAdapter = true)
data class CodesDto(
    @Json(name = "alpha_2") val alpha2: String? = null,
    @Json(name = "alpha_3") val alpha3: String? = null,
    val fifa: String? = null,
)

@JsonClass(generateAdapter = true)
data class FlagDto(
    @Json(name = "url_png") val urlPng: String? = null,
    @Json(name = "url_svg") val urlSvg: String? = null,
    val description: String? = null,
)

@JsonClass(generateAdapter = true)
data class CapitalDto(
    val name: String? = null,
)

@JsonClass(generateAdapter = true)
data class AreaDto(
    val kilometers: Double? = null,
    val miles: Double? = null,
)

@JsonClass(generateAdapter = true)
data class CurrencyItemDto(
    val code: String? = null,
    val name: String? = null,
    val symbol: String? = null,
)

@JsonClass(generateAdapter = true)
data class LinksDto(
    @Json(name = "google_maps") val googleMaps: String? = null,
    @Json(name = "open_street_maps") val openStreetMaps: String? = null,
)

@JsonClass(generateAdapter = true)
data class CarDto(
    @Json(name = "driving_side") val side: String? = null,
    val signs: List<String>? = null,
)

@JsonClass(generateAdapter = true)
data class ClassificationDto(
    @Json(name = "un_member") val unMember: Boolean? = null,
)

@JsonClass(generateAdapter = true)
data class DateDto(
    @Json(name = "start_of_week") val startOfWeek: String? = null,
)

@JsonClass(generateAdapter = true)
data class PostalCodeDto(
    val format: String? = null,
    val regex: String? = null,
)

@JsonClass(generateAdapter = true)
data class FlagsDto(
    val png: String? = null,
    val svg: String? = null,
    val alt: String? = null,
)

@JsonClass(generateAdapter = true)
data class CoatOfArmsDto(
    val png: String? = null,
    val svg: String? = null,
)

@JsonClass(generateAdapter = true)
data class MapsDto(
    val googleMaps: String? = null,
    val openStreetMaps: String? = null,
)
