package com.sumup.countryapp.datamodels

import com.google.gson.annotations.SerializedName

data class CountryBasic(
    @SerializedName("cca2")
    val countryCode: String,
    val region: String? = null,
    val name: NameDto? = null,
    val flags: FlagsDto? = null,
    var isFavourite: Boolean = false
)
