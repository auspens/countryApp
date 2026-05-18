package com.sumup.countryapp.repository

import com.sumup.countryapp.datamodels.CountryDTOShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.String
import kotlin.collections.mutableSetOf


class CountryRepositoryImpl(
    val countryAppApi: com.sumup.countryapp.api.CountryAppApi
) : CountryRepository {
    private var _countries = MutableStateFlow<List<CountryDTOShort>>(emptyList())
    private var _regions = MutableStateFlow<List<String>>(emptyList())

    override val countries: StateFlow<List<CountryDTOShort>> = _countries

    override val regions: StateFlow<List<String>> = _regions

    override fun initRegions() {

        val setOfRegions = mutableSetOf<String>()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            runCatching {
                val response = withContext(Dispatchers.IO) {
                    countryAppApi.getCountries("region")
                }
                if (response.isSuccessful) {
                    response.body()
                        ?.mapNotNull { response -> response.region?.let { setOfRegions.add(it) } }
                }
            }
        }
        _regions.value = setOfRegions.toList()
    }


    override suspend fun initCountries(fields: List<String>) {
        val response = withContext(Dispatchers.IO) {
            countryAppApi.getCountries(fields.joinToString(","))
        }
        if (response.isSuccessful) {
            val countryResponse = response.body()
            if (countryResponse != null) {
                _countries.value = countryResponse
            } else {
                throw (Exception("Empty response body"))
            }
        } else {
            throw (Exception("Error fetching countries: ${response.code()} ${response.message()}"))
        }
    }
}