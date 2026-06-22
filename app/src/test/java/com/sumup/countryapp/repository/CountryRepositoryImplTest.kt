package com.sumup.countryapp.repository

import com.sumup.countryapp.api.CountryAppApi
import com.sumup.countryapp.datamodels.CountriesListApiResponse
import com.sumup.countryapp.datamodels.CountriesListData
import com.sumup.countryapp.datamodels.ResponseMeta
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
        coEvery {
            countryAppApi.getCountries(
                fields = "names.common,codes.alpha_2,flag.url_png,region",
                limit = 100,
                offset = 0,
            )
        } throws Exception("Network error")

        val result = countryRepository.fetchCountriesAndRegions()

        coVerify {
            countryAppApi.getCountries(
                fields = "names.common,codes.alpha_2,flag.url_png,region",
                limit = 100,
                offset = 0,
            )
        }
        assert(result.isFailure)
    }

    @Test
    fun `fetchCountriesAndRegions returns Failure when API response body is null`() = runTest {
        coEvery {
            countryAppApi.getCountries(
                fields = "names.common,codes.alpha_2,flag.url_png,region",
                limit = 100,
                offset = 0,
            )
        } returns Response.success(null)

        val result = countryRepository.fetchCountriesAndRegions()

        coVerify {
            countryAppApi.getCountries(
                fields = "names.common,codes.alpha_2,flag.url_png,region",
                limit = 100,
                offset = 0,
            )
        }
        assert(result.isFailure)
    }

    @Test
    fun `fetchCountriesAndRegions returns Success when API response body is not null`() = runTest {
        coEvery {
            countryAppApi.getCountries(
                fields = "names.common,codes.alpha_2,flag.url_png,region",
                limit = 100,
                offset = 0,
            )
        } returns Response.success(
            CountriesListApiResponse(
                data = CountriesListData(
                    objects = emptyList(),
                    meta = ResponseMeta(more = false),
                ),
            ),
        )

        val result = countryRepository.fetchCountriesAndRegions()

        coVerify {
            countryAppApi.getCountries(
                fields = "names.common,codes.alpha_2,flag.url_png,region",
                limit = 100,
                offset = 0,
            )
        }
        assert(result.isSuccess)
    }
}
