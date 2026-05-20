package com.sumup.countryapp.datamodels

data class CountryFull(

//    @SerializedName("tld") val tld: List<String>? = null,
    val cca2: String? = null,
//    @SerializedName("ccn3") val ccn3: String? = null,
//    @SerializedName("cca3") val cca3: String? = null,
//    @SerializedName("cioc") val cioc: String? = null,

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

    // Dynamic keys: "PEN": { "symbol": "...", "name": "..." }
    val currencies: Map<String, CurrencyDto>? = null,

    // Dynamic keys: "spa": "Spanish"
    val languages: Map<String, String>? = null,

//    @SerializedName("latlng") val latlng: List<Double>? = null,
//    @SerializedName("demonyms") val demonyms: DemonymsDto? = null,

    // Dynamic keys: "ara": { official/common }, ...
//    @SerializedName("translations") val translations: Map<String, TranslationDto>? = null,

    // Dynamic keys: "2019": 41.5
    val gini: Map<String, Double>? = null,

    val flags: FlagsDto? = null,
    val coatOfArms: CoatOfArmsDto? = null,

    val startOfWeek: String? = null,
    val capitalInfo: CapitalInfoDto? = null,
    val postalCode: PostalCodeDto? = null
)

data class IddDto(
    val root: String? = null,
    val suffixes: List<String>? = null
)

data class MapsDto(
    val googleMaps: String? = null,
    val openStreetMaps: String? = null
)

data class CarDto(
    val signs: List<String>? = null,
    val side: String? = null
)

data class NameDto(
    val common: String? = null,
    val official: String? = null,

    // Dynamic keys: "spa": {official/common}, "que": {...}
    val nativeName: Map<String, NativeNameDto>? = null
)

data class NativeNameDto(
    val official: String? = null,
    val common: String? = null
)

data class CurrencyDto(
    val symbol: String? = null,
    val name: String? = null
)

//data class DemonymsDto(
//    @SerializedName("eng") val eng: GenderedDemonymDto? = null,
//    @SerializedName("fra") val fra: GenderedDemonymDto? = null
//)
//
//data class GenderedDemonymDto(
//    @SerializedName("f") val f: String? = null,
//    @SerializedName("m") val m: String? = null
//)
//
//data class TranslationDto(
//    @SerializedName("official") val official: String? = null,
//    @SerializedName("common") val common: String? = null
//)

data class FlagsDto(
    val png: String? = null,
    val svg: String? = null,
    val alt: String? = null
)

data class CoatOfArmsDto(
    val png: String? = null,
    val svg: String? = null
)

data class CapitalInfoDto(
    val latlng: List<Double>? = null
)

data class PostalCodeDto(
    val format: String? = null,
    val regex: String? = null
)

