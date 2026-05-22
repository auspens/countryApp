package com.sumup.countryapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface FavouritesRepository {
    suspend fun getFavouriteCountries(): Set<String>
    suspend fun addToFavourites(countryName: String)
    suspend fun removeFromFavourites(countryName: String)
}

class FavouritesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    FavouritesRepository {

    override suspend fun getFavouriteCountries(): Set<String> {

        try {
            val flow = dataStore.data.first()
            val favsSet = flow[KEY_NAME]
            return favsSet ?: emptySet()
        } catch (exception: Exception) {
            return emptySet()
        }

    }

    override suspend fun addToFavourites(countryName: String) {
        val currentFavs = getFavouriteCountries().toMutableSet()
        currentFavs.add(countryName)
        dataStore.edit {
            it[KEY_NAME] = currentFavs
        }
    }

    override suspend fun removeFromFavourites(countryName: String) {
        val currentFavs = getFavouriteCountries().toMutableSet()
        currentFavs.remove(countryName)
        dataStore.edit {
            it[KEY_NAME] = currentFavs
        }
    }


    private companion object {
        val KEY_NAME = stringSetPreferencesKey(
            name = "favourites"
        )
    }
}

