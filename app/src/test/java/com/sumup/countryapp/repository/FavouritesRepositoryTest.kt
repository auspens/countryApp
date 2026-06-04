package com.sumup.countryapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringSetPreferencesKey
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FavouritesRepositoryTest {
    private val dataStore: DataStore<Preferences> = mockk()
    private val favouritesRepository = FavouritesRepositoryImpl(dataStore)

    @Test
    fun `getFavouriteCountries returns empty set when data store throws exception`() = runTest{
        // Given
        every{ dataStore.data } throws Exception("Data store error")

        // When
        val result = runCatching { favouritesRepository.getFavouriteCountries() }

        // Then
        verify { dataStore.data }
        assert(result.isSuccess)
        assert(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getFavouriteCountries returns set of favourite countries when data store returns data`() = runTest {
        // Given
        val prefs = preferencesOf(stringSetPreferencesKey("favourites") to setOf("US", "GB"))
        every { dataStore.data } returns flowOf(prefs)

        // When
        val result = runCatching { favouritesRepository.getFavouriteCountries() }

        // Then
        verify { dataStore.data }
        assert(result.isSuccess)
        assert(result.getOrNull() == setOf("US", "GB"))
    }

    @Test
    fun `addToFavourites calls dataStore update`() = runTest {
        // Given
        val initialPrefs = preferencesOf(stringSetPreferencesKey("favourites") to setOf("US"))
        every { dataStore.data } returns flowOf(initialPrefs)
        coEvery { dataStore.updateData(any()) } returns initialPrefs

        // When
        favouritesRepository.addToFavourites("GB")

        // Then
        //edit is Kotlin extension function, compiler translates it to dataStore.updateData(),
        // so we need to verify that updateData is called
        coVerify { dataStore.updateData (any()) }
    }

    @Test
    fun `removeFromFavourites calls dataStore update`() = runTest {
        // Given
        val initialPrefs = preferencesOf(stringSetPreferencesKey("favourites") to setOf("US"))
        every { dataStore.data } returns flowOf(initialPrefs)
        coEvery { dataStore.updateData(any()) } returns initialPrefs

        // When
        favouritesRepository.removeFromFavourites("GB")

        // Then
        //edit is Kotlin extension function, compiler translates it to dataStore.updateData(),
        // so we need to verify that updateData is called
        coVerify { dataStore.updateData (any()) }
    }
}