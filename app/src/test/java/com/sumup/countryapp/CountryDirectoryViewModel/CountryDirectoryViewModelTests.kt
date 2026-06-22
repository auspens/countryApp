package com.sumup.countryapp.CountryDirectoryViewModel

import android.content.Context
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CountryFull
import com.sumup.countryapp.datamodels.NameDto
import com.sumup.countryapp.repository.CountryRepository
import com.sumup.countryapp.repository.FavouritesRepository
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import com.sumup.countryapp.viewmodel.CurrentScreen
import com.sumup.countryapp.viewmodel.DetailsUiState
import com.sumup.countryapp.viewmodel.HomeUiState
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CountryDirectoryViewModelTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val dispatcher = UnconfinedTestDispatcher()
    val repository: CountryRepository = mockk()
    val favouritesRepository: FavouritesRepository = mockk()
    val context: Context = mockk()


    @Test
    fun `on creation, fetchCountriesAndRegions is called and uiState is updated to Loading`() {
        // Given
        coEvery { repository.fetchCountriesAndRegions() } just Awaits
        coEvery { favouritesRepository.getFavouriteCountries() } just Awaits

        // When
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)


        // Then
        assert(viewModel.uiState.value is HomeUiState.Loading)
        coVerify { repository.fetchCountriesAndRegions() }
    }

    @Test
    fun `on creation, viewModel updates uiState to Data after fetching the countries and regions`() {
        //Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()

        // When
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // Then
        assert(viewModel.uiState.value is HomeUiState.Data)
        val dataState = (viewModel.uiState.value as HomeUiState.Data)
        assert(dataState.countries.toList() == mockCountries)
        assert(dataState.regions.toList() == mockRegions)
    }


    @Test
    fun `on creation, if fetching countries and regions fails, uiState is updated to Error`() {
        // Given
        coEvery { repository.fetchCountriesAndRegions() } returns Result.failure(Exception("Network error"))
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()

        // When
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // Then
        assert(viewModel.uiState.value is HomeUiState.Error)
    }

    @Test
    fun `applyFilter with "All" filter updates uiState to show all countries`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.applyFilter("All")

        // Then
        assert(viewModel.uiState.value is HomeUiState.Data)
        val dataState = (viewModel.uiState.value as HomeUiState.Data)
        assert(dataState.countries.toList() == mockCountries)
    }

    @Test
    fun `applyFilter with specific region updates uiState to show only countries from that region`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.applyFilter("Americas")

        // Then
        assert(viewModel.uiState.value is HomeUiState.Data)
        val dataState = (viewModel.uiState.value as HomeUiState.Data)
        assert(dataState.countries.toList() == mockCountries.filter { it.region == "Americas" })
    }

    @Test
    fun `Applying filter with non-existent region results in empty country list`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.applyFilter("Asia")

        // Then
        assert(viewModel.uiState.value is HomeUiState.Data)
        val dataState = (viewModel.uiState.value as HomeUiState.Data)
        assert(dataState.countries.isEmpty())
    }

    @Test
    fun  `toggleFavorite updates the favourite status of the country and calls the repository to update it`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()
        coEvery { repository.updateFavouriteStatus(any(), any()) } just Runs
        coEvery { favouritesRepository.addToFavourites(any()) } just Runs
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.toggleFavorite("US")

        // Then
        assert(viewModel.uiState.value is HomeUiState.Data)
        coVerify { repository.updateFavouriteStatus("US", true) }
        coVerify { favouritesRepository.addToFavourites("US") }
    }

    @Test
    fun `switchToFavourites updates the uiState to show only favourite countries`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        val mockFavourites = setOf("US", "FR")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns mockFavourites
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.switchToFavourites()

        // Then
        assert(viewModel.uiState.value is HomeUiState.Data)
        val dataState = (viewModel.uiState.value as HomeUiState.Data)
        assert(dataState.countries.toList().map{it.countryCode} == mockFavourites.toList())
        assert(dataState.currentScreen == CurrentScreen.Saved)
    }

    @Test
    fun `switchToCountryDetails updates the uiState to show the details of the selected country`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()
        coEvery { repository.fetchCountryDetailsByCode("US") } returns Result.success(CountryFull("US"))
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.switchToCountryDetails("US")

        // Then
        coVerify { repository.fetchCountryDetailsByCode("US") }
        assert(viewModel.detailsUiState.value is DetailsUiState.Data)
        val dataState = (viewModel.detailsUiState.value as DetailsUiState.Data)
        assert(dataState.currentScreen == CurrentScreen.Details)
        assert(dataState.countryInfo.countryCode == "US")
    }

    @Test
    fun `switchToCountryDetails with non-existent country code updates uiState to Error`() {
        // Given
        val mockCountries = getMockCountries()
        val mockRegions = listOf("Americas", "Europe")
        coEvery { repository.fetchCountriesAndRegions() } returns Result.success(mockCountries)
        coEvery { repository.countries } returns mockCountries
        coEvery { repository.regions } returns mockRegions
        coEvery { favouritesRepository.getFavouriteCountries() } returns emptySet()
        coEvery { repository.fetchCountryDetailsByCode("XX") } returns Result.failure(Exception("Country not found"))
        val viewModel = CountryDirectoryViewModel(repository, favouritesRepository, context)

        // When
        viewModel.switchToCountryDetails("XX")

        // Then
        coVerify { repository.fetchCountryDetailsByCode("XX") }
        assert(viewModel.detailsUiState.value is DetailsUiState.Error)
    }
}



fun getMockCountries(): List<CountryBasic> {
    return listOf(
        CountryBasic(countryCode = "US", commonName = "United States", region = "Americas"),
        CountryBasic(countryCode = "FR", commonName = "France", region = "Europe"),
        CountryBasic(countryCode = "BR", commonName = "Brazil", region = "Americas"),
        CountryBasic(countryCode = "DE", commonName = "Germany", region = "Europe"),
        CountryBasic(countryCode = "CA", commonName = "Canada", region = "Americas"),
    )
}