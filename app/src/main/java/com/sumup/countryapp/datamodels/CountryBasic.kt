package com.sumup.countryapp.datamodels

data class CountryBasic(

    val region: String? = null,
    val name: NameDto? = null,
    val flags: FlagsDto? = null,
    var isFavourite: Boolean = false
)
