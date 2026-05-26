package com.sumup.countryapp.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import com.sumup.countryapp.viewmodel.HomeUiState

@Composable
internal fun CountriesList(
    state: HomeUiState.Data,
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
//            .padding(6.dp)
            .fillMaxWidth(),
    ) {
        LazyRow() {
            items(
                items = (state).regions,
                key = { item -> item.hashCode() }
            ) { region ->
                RegionFilterChip(
                    region,
                    (state).filter,
                    { viewModel.applyFilter(region) })
            }

        }
        LazyColumn() {
            items(
                items = (state).countries,
                key = { item -> item.hashCode() },
            ) { country ->
                CountryItem(
                    country = country, modifier = Modifier.Companion, clickAction = {},
                    toggleFavorites = { viewModel.toggleFavorite(country.name?.common ?: "") },
                    isFavourite = (state).saved.contains(country.name?.common ?: "")
                )
            }
        }
    }
}