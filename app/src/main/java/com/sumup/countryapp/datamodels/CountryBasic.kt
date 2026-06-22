package com.sumup.countryapp.datamodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryBasic(
    val names: NamesDto? = null,
    val codes: CodesDto? = null,
    val flag: FlagDto? = null,
    val region: String? = null,
    var isFavourite: Boolean = false,
) {
    val countryCode: String?
        get() = codes?.alpha2?.takeIf { it.isNotBlank() }

    val commonName: String?
        get() = names?.common

    val officialName: String?
        get() = names?.official

    val flagPng: String?
        get() = flag?.urlPng

    val flagSvg: String?
        get() = flag?.urlSvg

    val name: NamesDto?
        get() = names

    val flags: FlagsDto?
        get() = flag?.urlPng?.let {
            FlagsDto(png = it, svg = flag?.urlSvg, alt = flag?.description)
        }
}
