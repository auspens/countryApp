package com.sumup.countryapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sumup.countryapp.R
import com.sumup.countryapp.ui.theme.Brown
import com.sumup.countryapp.viewmodel.HomeUiState

@Composable
internal fun CountriesList(
    state: HomeUiState.Data,
    modifier: Modifier,
    onShowAllCountriesClick: () -> Unit = {},
    onToggleFavourite: (String)-> Unit,
    onApplyFilter: (String)-> Unit,
    onChooseCountryClick: (String)-> Unit,
) {
    if (state.countries.isEmpty()) {
        ListIsEmpty(modifier, onShowAllCountriesClick)
        return
    }
    Column(
        modifier = modifier
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
                    { onApplyFilter(region) })
            }

        }
        LazyColumn() {
            items(
                items = (state).countries,
                key = { item -> item.countryCode!! },
            ) { country ->
                CountryItem(
                    country = country, modifier = Modifier.Companion, clickAction = {onChooseCountryClick(country.countryCode!!)},
                    toggleFavorites = { onToggleFavourite(country.countryCode!!) }
                )
            }
        }
    }
}

@Composable
internal fun ListIsEmpty(
    modifier: Modifier = Modifier,
    onShowAllCountriesClick: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.ic_star_outlined),
            contentDescription = null,
            tint = Brown,
            modifier = Modifier.Companion.size(125.dp)
        )
        Text(
            text = "No saved countries yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Start exploring the world and save\n" +
                    "your favorite countries to see them\n" +
                    "here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onShowAllCountriesClick,
            content = {
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.Companion.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Explore countries", color = MaterialTheme.colorScheme.onPrimary) },
            )
    }
}