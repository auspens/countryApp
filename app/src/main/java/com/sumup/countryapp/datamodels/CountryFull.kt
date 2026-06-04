package com.sumup.countryapp.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class CountryFull(
    @Json(name = "cca2")
    val countryCode: String? = null,

    val independent: Boolean? = null,
    val status: String? = null,
    val unMember: Boolean? = null,

    val idd: IddDto? = null,
    val capital: List<String>? = null,
    val altSpellings: List<String>? = null,

    val region: String? = null,
    val subregion: String? = null,
    val landlocked: Boolean? = null,
    val borders: List<String>? = null,


    val area: Double? = null,
    val maps: MapsDto? = null,
    val population: Long? = null,

    val fifa: String? = null,
    val car: CarDto? = null,

    val timezones: List<String>? = null,
    val continents: List<String>? = null,

    val flag: String? = null,
    val name: NameDto? = null,

    val currencies: Map<String, CurrencyDto>? = null,

    val languages: Map<String, String>? = null,

    val gini: Map<String, Double>? = null,

    val flags: FlagsDto? = null,
    val coatOfArms: CoatOfArmsDto? = null,

    val startOfWeek: String? = null,
    val capitalInfo: CapitalInfoDto? = null,
    val postalCode: PostalCodeDto? = null,

    var isFavourite: Boolean = false
)
@JsonClass(generateAdapter = true)
data class IddDto(
    val root: String? = null,
    val suffixes: List<String>? = null
)
@JsonClass(generateAdapter = true)
data class MapsDto(
    val googleMaps: String? = null,
    val openStreetMaps: String? = null
)
@JsonClass(generateAdapter = true)
data class CarDto(
    val signs: List<String>? = null,
    val side: String? = null
)
@JsonClass(generateAdapter = true)
data class NameDto(
    val common: String? = null,
    val official: String? = null,
    val nativeName: Map<String, NativeNameDto>? = null
)
@JsonClass(generateAdapter = true)
data class NativeNameDto(
    val official: String? = null,
    val common: String? = null
)
@JsonClass(generateAdapter = true)
data class CurrencyDto(
    val symbol: String? = null,
    val name: String? = null
)
@JsonClass(generateAdapter = true)
data class FlagsDto(
    val png: String? = null,
    val svg: String? = null,
    val alt: String? = null
)
@JsonClass(generateAdapter = true)
data class CoatOfArmsDto(
    val png: String? = null,
    val svg: String? = null
)
@JsonClass(generateAdapter = true)
data class CapitalInfoDto(
    val latlng: List<Double>? = null
)
@JsonClass(generateAdapter = true)
data class PostalCodeDto(
    val format: String? = null,
    val regex: String? = null
)

