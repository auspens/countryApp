package com.sumup.countryapp.repository

import com.sumup.countryapp.api.CountryAppApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response


class CountryRepositoryImplTest {

    @MockK
    private val countryAppApi: CountryAppApi = mockk()
    private lateinit var countryRepository: CountryRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        countryRepository = CountryRepositoryImpl(countryAppApi)
    }

    @Test
    fun `fetchCountriesAndRegions returns Failure when API call throws `() = runTest {

        //Given
        coEvery { countryAppApi.getCountries("cca2,name,flags,region") } throws Exception("Network error")

        //When
        val result = countryRepository.fetchCountriesAndRegions()

        //Then
        coVerify { countryAppApi.getCountries("cca2,name,flags,region") }
        assert (result.isFailure)
    }

    @Test
    fun `fetchCountriesAndRegions returns Failure when API response body is null`() = runTest {
        //Given
        coEvery { countryAppApi.getCountries("cca2,name,flags,region") } returns Response.success(null)

        //When
        val result = countryRepository.fetchCountriesAndRegions()

        //Then
        coVerify { countryAppApi.getCountries("cca2,name,flags,region") }
        assert (result.isFailure)
    }

    @Test
    fun `fetchCountriesAndRegions returns Success when API response body is not null`() = runTest {
        //Given
        coEvery { countryAppApi.getCountries("cca2,name,flags,region") } returns Response.success(emptyList())

        //When
        val result = countryRepository.fetchCountriesAndRegions()

        //Then
        coVerify { countryAppApi.getCountries("cca2,name,flags,region") }
        assert (result.isSuccess)
    }
}