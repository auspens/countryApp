package com.sumup.countryapp.api

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryAppApiIntegrationTest {

    private val api: CountryAppApi = Retrofit.Builder()
        .baseUrl("https://restcountries.com/v3.1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CountryAppApi::class.java)

    @Test
    fun `getCountryByName returns a valid response for Germany`() = runBlocking {
        val response = api.getCountryByName("Germany")

        assertTrue("Expected successful response", response.isSuccessful)
        assertNotNull("Expected non-null body", response.body())
        val country = response.body()?.get(0)
        assertNotNull(country?.name?.common)
        println("Country name: ${country?.name?.common}") // visible in test output
    }
}