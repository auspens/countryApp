package com.sumup.countryapp.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class CountryBasic(
    @Json(name = "cca2")
    val countryCode: String? = null,
    val region: String? = null,
    val name: NameDto? = null,
    val flags: FlagsDto? = null,
    var isFavourite: Boolean = false
)
