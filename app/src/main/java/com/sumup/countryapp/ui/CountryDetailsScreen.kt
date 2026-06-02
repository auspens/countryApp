package com.sumup.countryapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.sumup.countryapp.R
import com.sumup.countryapp.datamodels.CoatOfArmsDto
import com.sumup.countryapp.datamodels.CountryFull
import com.sumup.countryapp.datamodels.CurrencyDto
import com.sumup.countryapp.datamodels.NameDto
import com.sumup.countryapp.ui.theme.CountryAppTheme
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import com.sumup.countryapp.viewmodel.DetailsUiState

@Composable
internal fun CountryDetailsScreen(
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier = Modifier,
    toggleFavourites: () -> Unit
) {
    val state by viewModel.detailsUiState.collectAsStateWithLifecycle()
    when (state) {
        is DetailsUiState.Data -> {
            CountryDetails(
                countryInfo = (state as DetailsUiState.Data).countryInfo,
                modifier = modifier,
                toggleFavourites = toggleFavourites
            )
        }

        is DetailsUiState.Loading -> {
            Column(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(125.dp))
                CircularProgressIndicator()
                Text("Loading...")
            }
        }

        else -> Text(text = "Error loading country details")
    }

}

@SuppressLint("DefaultLocale")
@Composable
fun CountryDetails(
    countryInfo: CountryFull,
    modifier: Modifier = Modifier,
    toggleFavourites: () -> Unit
) {
    LazyColumn(
        modifier
            .paint(
                painter = rememberAsyncImagePainter(countryInfo.coatOfArms?.png),
                contentScale = ContentScale.None,
                alignment = Alignment.TopCenter,
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(140.dp))
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column() {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "COUNTRY PROFILE",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Text(
                                countryInfo.name?.common ?: "",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Row(modifier = Modifier.padding(bottom = 6.dp)) {
                                Icon(
                                    Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    countryInfo.region ?: "Unknown",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .align(Alignment.Bottom)
                                )
                            }
                        }
                        FavouritesIcon(toggleFavourites, countryInfo.isFavourite)
                    }
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                    ) {
                        InfoCard(
                            title = "CAPITAL", content = countryInfo.capital?.getOrNull(0) ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        InfoCard(
                            title = "POPULATION",
                            content = if (countryInfo.population != null) String.format(
                                "%.1f",
                                (countryInfo.population) / 1000000f
                            ) + "M" else "Unknown",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(IntrinsicSize.Max)
                    ) {
                        InfoCard(
                            title = "CURRENCY",
                            content = countryInfo.currencies?.map { (string, dto) ->
                                "${dto.name} (${dto.symbol})"
                            }?.joinToString("/n") ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        InfoCard(
                            title = "REGION", content = countryInfo.region ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                }
            }
        }
        item {
            CountryDescription(
                title = "About ${countryInfo.name?.common}",
                content = "some description"
            )
        }

    }
}

@Composable
internal fun CountryDescription(title: String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun InfoCard(title: String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryDetailsPreview() {
    CountryAppTheme() {
        CountryDetails(
            countryInfo = CountryFull(
                name = NameDto(
                    common = "Spain",
                    official = "Kingdom of Spain"
                ),
                coatOfArms = CoatOfArmsDto(
                    png = "https://upload.wikimedia.org/wikipedia/en/thumb/9/93/Coat_of_arms_of_Spain.svg/1200px-Coat_of_arms_of_Spain.svg.png"
                ),
                region = "Europe",
                population = 47351567,
                capital = listOf("Madrid"),
                currencies = mapOf(
                    "EUR" to CurrencyDto(
                        name = "Euro",
                        symbol = "€"
                    )
                )
            ),
            toggleFavourites = {}
        )
    }
}