package com.sumup.countryapp.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryFull(
    @Json(name = "codes.alpha_2")
    val countryCode: String? = null,

    @Json(name = "names.common")
    val commonName: String? = null,

    @Json(name = "names.official")
    val officialName: String? = null,

    val region: String? = null,
    val subregion: String? = null,
    val landlocked: Boolean? = null,
    val borders: List<String>? = null,

    @Json(name = "area.kilometers")
    val areaKilometers: Double? = null,

    val population: Long? = null,

    @Json(name = "codes.fifa")
    val fifa: String? = null,

    val timezones: List<String>? = null,
    val continents: List<String>? = null,

    val capitals: List<CapitalDto>? = null,

    val currencies: Map<String, CurrencyDto>? = null,

    @Json(name = "calling_codes")
    val callingCodes: List<String>? = null,

    @Json(name = "flag.url_png")
    val flagPng: String? = null,

    @Json(name = "flag.url_svg")
    val flagSvg: String? = null,

    @Json(name = "flag.description")
    val flagDescription: String? = null,

    @Json(name = "links.google_maps")
    val googleMaps: String? = null,

    @Json(name = "links.open_street_maps")
    val openStreetMaps: String? = null,

    @Json(name = "cars.driving_side")
    val drivingSide: String? = null,

    @Json(name = "cars.signs")
    val carSigns: List<String>? = null,

    @Json(name = "classification.un_member")
    val unMember: Boolean? = null,

    @Json(name = "date.start_of_week")
    val startOfWeek: String? = null,

    @Json(name = "postal_code.format")
    val postalCodeFormat: String? = null,

    @Json(name = "postal_code.regex")
    val postalCodeRegex: String? = null,

    var isFavourite: Boolean = false,
) {
    val name: NameDto?
        get() = commonName?.let { NameDto(common = it, official = officialName) }

    val capital: List<String>?
        get() = capitals?.mapNotNull { it.name }

    val flags: FlagsDto?
        get() = flagPng?.let { FlagsDto(png = it, svg = flagSvg, alt = flagDescription) }

    val coatOfArms: CoatOfArmsDto?
        get() = flagPng?.let { CoatOfArmsDto(png = it) }

    val area: Double?
        get() = areaKilometers

    val maps: MapsDto?
        get() = if (googleMaps != null || openStreetMaps != null) {
            MapsDto(googleMaps = googleMaps, openStreetMaps = openStreetMaps)
        } else {
            null
        }

    val car: CarDto?
        get() = if (drivingSide != null || carSigns != null) {
            CarDto(side = drivingSide, signs = carSigns)
        } else {
            null
        }

    val postalCode: PostalCodeDto?
        get() = if (postalCodeFormat != null || postalCodeRegex != null) {
            PostalCodeDto(format = postalCodeFormat, regex = postalCodeRegex)
        } else {
            null
        }
}

@JsonClass(generateAdapter = true)
data class CapitalDto(
    val name: String? = null,
)

@JsonClass(generateAdapter = true)
data class MapsDto(
    val googleMaps: String? = null,
    val openStreetMaps: String? = null,
)

@JsonClass(generateAdapter = true)
data class CarDto(
    val signs: List<String>? = null,
    val side: String? = null,
)

@JsonClass(generateAdapter = true)
data class NameDto(
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
data class CurrencyDto(
    val symbol: String? = null,
    val name: String? = null,
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
data class PostalCodeDto(
    val format: String? = null,
    val regex: String? = null,
)
