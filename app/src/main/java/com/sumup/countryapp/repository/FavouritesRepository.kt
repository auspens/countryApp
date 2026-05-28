package com.sumup.countryapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface FavouritesRepository {
    suspend fun getFavouriteCountries(): Set<String>
    suspend fun addToFavourites(cca2: String)
    suspend fun removeFromFavourites(cca2: String)
}

class FavouritesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    FavouritesRepository {

    override suspend fun getFavouriteCountries(): Set<String> {

        try {
            val flow = dataStore.data.first()
            val favsSet:Set<String>? = flow[KEY_NAME]
            return favsSet ?: emptySet<String>()
        } catch (exception: Exception) {
            return emptySet<String>()
        }

    }

    override suspend fun addToFavourites(cca2: String) {
        val currentFavs = getFavouriteCountries().toMutableSet()
        currentFavs.add(cca2)
        dataStore.edit {
            it[KEY_NAME] = currentFavs
        }
    }

    override suspend fun removeFromFavourites(cca2: String) {
        val currentFavs = getFavouriteCountries().toMutableSet()
        currentFavs.remove(cca2)
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

