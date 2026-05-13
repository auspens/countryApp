package com.sumup.countryapp.datamodels

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("tld") val tld: List<String>? = null,
    @SerializedName("cca2") val cca2: String? = null,
    @SerializedName("ccn3") val ccn3: String? = null,
    @SerializedName("cca3") val cca3: String? = null,
    @SerializedName("cioc") val cioc: String? = null,

    @SerializedName("independent") val independent: Boolean? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("unMember") val unMember: Boolean? = null,

    @SerializedName("idd") val idd: IddDto? = null,
    @SerializedName("capital") val capital: List<String>? = null,
    @SerializedName("altSpellings") val altSpellings: List<String>? = null,

    @SerializedName("region") val region: String? = null,
    @SerializedName("subregion") val subregion: String? = null,
    @SerializedName("landlocked") val landlocked: Boolean? = null,
    @SerializedName("borders") val borders: List<String>? = null,

    @SerializedName("area") val area: Double? = null,
    @SerializedName("maps") val maps: MapsDto? = null,
    @SerializedName("population") val population: Long? = null,

    @SerializedName("fifa") val fifa: String? = null,
    @SerializedName("car") val car: CarDto? = null,

    @SerializedName("timezones") val timezones: List<String>? = null,
    @SerializedName("continents") val continents: List<String>? = null,

    @SerializedName("flag") val flag: String? = null,
    @SerializedName("name") val name: NameDto? = null,

    // Dynamic keys: "PEN": { "symbol": "...", "name": "..." }
    @SerializedName("currencies") val currencies: Map<String, CurrencyDto>? = null,

    // Dynamic keys: "spa": "Spanish"
    @SerializedName("languages") val languages: Map<String, String>? = null,

    @SerializedName("latlng") val latlng: List<Double>? = null,
    @SerializedName("demonyms") val demonyms: DemonymsDto? = null,

    // Dynamic keys: "ara": { official/common }, ...
    @SerializedName("translations") val translations: Map<String, TranslationDto>? = null,

    // Dynamic keys: "2019": 41.5
    @SerializedName("gini") val gini: Map<String, Double>? = null,

    @SerializedName("flags") val flags: FlagsDto? = null,
    @SerializedName("coatOfArms") val coatOfArms: CoatOfArmsDto? = null,

    @SerializedName("startOfWeek") val startOfWeek: String? = null,
    @SerializedName("capitalInfo") val capitalInfo: CapitalInfoDto? = null,
    @SerializedName("postalCode") val postalCode: PostalCodeDto? = null
)

data class IddDto(
    @SerializedName("root") val root: String? = null,
    @SerializedName("suffixes") val suffixes: List<String>? = null
)

data class MapsDto(
    @SerializedName("googleMaps") val googleMaps: String? = null,
    @SerializedName("openStreetMaps") val openStreetMaps: String? = null
)

data class CarDto(
    @SerializedName("signs") val signs: List<String>? = null,
    @SerializedName("side") val side: String? = null
)

data class NameDto(
    @SerializedName("common") val common: String? = null,
    @SerializedName("official") val official: String? = null,

    // Dynamic keys: "spa": {official/common}, "que": {...}
    @SerializedName("nativeName") val nativeName: Map<String, NativeNameDto>? = null
)

data class NativeNameDto(
    @SerializedName("official") val official: String? = null,
    @SerializedName("common") val common: String? = null
)

data class CurrencyDto(
    @SerializedName("symbol") val symbol: String? = null,
    @SerializedName("name") val name: String? = null
)

data class DemonymsDto(
    @SerializedName("eng") val eng: GenderedDemonymDto? = null,
    @SerializedName("fra") val fra: GenderedDemonymDto? = null
)

data class GenderedDemonymDto(
    @SerializedName("f") val f: String? = null,
    @SerializedName("m") val m: String? = null
)

data class TranslationDto(
    @SerializedName("official") val official: String? = null,
    @SerializedName("common") val common: String? = null
)

data class FlagsDto(
    @SerializedName("png") val png: String? = null,
    @SerializedName("svg") val svg: String? = null,
    @SerializedName("alt") val alt: String? = null
)

data class CoatOfArmsDto(
    @SerializedName("png") val png: String? = null,
    @SerializedName("svg") val svg: String? = null
)

data class CapitalInfoDto(
    @SerializedName("latlng") val latlng: List<Double>? = null
)

data class PostalCodeDto(
    @SerializedName("format") val format: String? = null,
    @SerializedName("regex") val regex: String? = null
)
