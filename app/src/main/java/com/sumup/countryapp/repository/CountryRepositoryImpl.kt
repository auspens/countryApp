package com.sumup.countryapp.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.sumup.countryapp.api.CountryAppApi
import com.sumup.countryapp.datamodels.CountriesListApiResponse
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryDetailApiResponse
import com.sumup.countryapp.datamodels.CountryFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CountryRepositoryImpl @Inject constructor(
    val countryAppApi: CountryAppApi
) : CountryRepository {
    private var _countries = mutableStateListOf<CountryBasic>()
    private var _regions = mutableStateListOf("All")

    override val countries: List<CountryBasic>
        get() = _countries

    override val regions: List<String>
        get() = _regions

    override suspend fun fetchCountriesAndRegions(): Result<List<CountryBasic>> {
        return runCatching {
            withContext(Dispatchers.IO) {
                fetchAllCountries()
            }
        }.mapCatching { countriesResponse ->
            saveCountriesAndRegions(countriesResponse)
            _countries
        }
    }

    private suspend fun fetchAllCountries(): List<CountryBasic> {
        val allCountries = mutableListOf<CountryBasic>()
        var offset = 0
        var hasMore = true

        while (hasMore) {
            val response = countryAppApi.getCountries(
                fields = LIST_RESPONSE_FIELDS,
                limit = PAGE_LIMIT,
                offset = offset,
            )
            val countriesPage = unwrapListResponse(response)
            allCountries.addAll(countriesPage)
            hasMore = response.body()?.data?.meta?.more == true
            offset += PAGE_LIMIT
        }

        return allCountries
    }

    private fun unwrapListResponse(response: retrofit2.Response<CountriesListApiResponse>): List<CountryBasic> {
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string() ?: "Unknown error")
        }

        val body = response.body() ?: throw Exception("Response body is null")
        body.errors?.firstOrNull()?.message?.let { throw Exception(it) }

        return body.data?.objects ?: throw Exception("No country data")
    }

    private fun unwrapDetailResponse(response: retrofit2.Response<CountryDetailApiResponse>): CountryFull {
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string() ?: "Unknown error")
        }

        val body = response.body() ?: throw Exception("Response body is null")
        body.errors?.firstOrNull()?.message?.let { throw Exception(it) }

        return body.data?.objects?.firstOrNull()
            ?: throw Exception("Country not found")
    }

    private fun saveCountriesAndRegions(countriesResponse: List<CountryBasic>) {
        val setOfRegions = mutableSetOf<String>()
        _countries = countriesResponse
            .filter { countryBasic -> countryBasic.countryCode != null }
            .toMutableStateList()
        _countries.map { country ->
            country.region?.let {
                setOfRegions.add(it)
            }
        }
        _regions.addAll(setOfRegions)
    }

    override suspend fun fetchCountryDetailsByCode(countryCode: String): Result<CountryFull> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val response = countryAppApi.getCountryByCode(countryCode)
                unwrapDetailResponse(response)
            }
        }
    }

    override fun updateFavouriteStatus(countryCode: String, isFavourite: Boolean) {
        val index = _countries.indexOfFirst { it.countryCode == countryCode }
        if (index != -1) {
            _countries[index] = _countries[index].copy(isFavourite = isFavourite)
        }
    }

    private companion object {
        const val LIST_RESPONSE_FIELDS = "names.common,codes.alpha_2,flag.url_png,region"
        const val PAGE_LIMIT = 100
    }
}
