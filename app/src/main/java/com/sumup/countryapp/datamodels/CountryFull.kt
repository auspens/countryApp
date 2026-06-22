package com.sumup.countryapp.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryFull(
    val names: NamesDto? = null,
    val codes: CodesDto? = null,
    val flag: FlagDto? = null,
    val region: String? = null,
    val subregion: String? = null,
    val landlocked: Boolean? = null,
    val borders: List<String>? = null,
    val area: AreaDto? = null,
    val population: Long? = null,
    val timezones: List<String>? = null,
    val continents: List<String>? = null,
    val capitals: List<CapitalDto>? = null,
    val currencies: List<CurrencyItemDto>? = null,
    @Json(name = "calling_codes")
    val callingCodes: List<String>? = null,
    val links: LinksDto? = null,
    val cars: CarDto? = null,
    val classification: ClassificationDto? = null,
    val date: DateDto? = null,
    @Json(name = "postal_code")
    val postalCode: PostalCodeDto? = null,
    var isFavourite: Boolean = false,
) {
    val countryCode: String?
        get() = codes?.alpha2?.takeIf { it.isNotBlank() }

    val commonName: String?
        get() = names?.common

    val officialName: String?
        get() = names?.official

    val name: NamesDto?
        get() = names

    val capital: List<String>?
        get() = capitals?.mapNotNull { it.name }

    val flags: FlagsDto?
        get() = flag?.urlPng?.let {
            FlagsDto(png = it, svg = flag?.urlSvg, alt = flag?.description)
        }

    val coatOfArms: CoatOfArmsDto?
        get() = flag?.urlPng?.let { CoatOfArmsDto(png = it) }

    val areaKilometers: Double?
        get() = area?.kilometers

    val maps: MapsDto?
        get() = links?.let {
            MapsDto(googleMaps = it.googleMaps, openStreetMaps = it.openStreetMaps)
        }

    val car: CarDto?
        get() = cars

    val unMember: Boolean?
        get() = classification?.unMember

    val startOfWeek: String?
        get() = date?.startOfWeek

    val fifa: String?
        get() = codes?.fifa
}
