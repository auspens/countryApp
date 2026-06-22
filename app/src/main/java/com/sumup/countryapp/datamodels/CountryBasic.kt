package com.sumup.countryapp.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryBasic(
    @Json(name = "codes.alpha_2")
    val countryCode: String? = null,

    @Json(name = "names.common")
    val commonName: String? = null,

    @Json(name = "names.official")
    val officialName: String? = null,

    val region: String? = null,

    @Json(name = "flag.url_png")
    val flagPng: String? = null,

    @Json(name = "flag.url_svg")
    val flagSvg: String? = null,

    var isFavourite: Boolean = false,
) {
    val name: NameDto?
        get() = commonName?.let { NameDto(common = it, official = officialName) }

    val flags: FlagsDto?
        get() = flagPng?.let { FlagsDto(png = it, svg = flagSvg) }
}
