//package com.sumup.countryapp.repository
//
//import com.sumup.countryapp.api.CountryAppApi
//import com.sumup.countryapp.datamodels.CountryDTOShort
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertTrue
//import org.junit.Test
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//import retrofit2.Response
//
//class CountryRepositoryImplTest {
//
//    private val api: CountryAppApi = mock(CountryAppApi::class.java)
//    private val repository = CountryRepositoryImpl(api)
//
//    @Test
//    fun `getCountryByName returns success when api returns body`() = runBlocking {
//        val country = CountryDTOShort(
//            cca2 = "US",
//            name = com.sumup.countryapp.datamodels.NameDto(common = "United States")
//        )
//
//        `when`(api.getCountryByName("USA")).thenReturn(Response.success(listOf(country)))
//
//        val result = repository.getCountryByName("USA")
//
//        assertTrue(result.isSuccess)
//        assertEquals(country, result.getOrNull())
//    }
//}