package com.sumup.countryapp.ui

import android.R.attr.background
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.sumup.countryapp.R
import com.sumup.countryapp.datamodels.CapitalDto
import com.sumup.countryapp.datamodels.CountryFull
import com.sumup.countryapp.datamodels.CurrencyItemDto
import com.sumup.countryapp.datamodels.FlagDto
import com.sumup.countryapp.datamodels.NamesDto
import com.sumup.countryapp.ui.theme.CountryAppTheme
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import com.sumup.countryapp.viewmodel.DetailsUiState

@Composable
internal fun CountryDetailsScreen(
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier = Modifier,
    toggleFavourites: () -> Unit,
    retry: () -> Unit
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

        is DetailsUiState.Loading -> LoadingPage()
        else -> ErrorPage(
            modifier = Modifier.padding(6.dp),
            retry = retry
        )
    }

}

@SuppressLint("DefaultLocale")
@Composable
internal fun CountryDetails(
    countryInfo: CountryFull,
    modifier: Modifier = Modifier,
    toggleFavourites: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .background(MaterialTheme.colorScheme.surfaceVariant)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            SubcomposeAsyncImage(
                model = countryInfo.coatOfArms?.png,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(screenHeight * 0.5f),
                contentScale = ContentScale.Fit,
                alignment = Alignment.TopCenter,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 3.dp
                    )
                },
                error = {
                    Text("Image failed to load")
                }
            )
        }
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .offset(y = (-16).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                val (
                    profile, name, regionIcon, region, capital, population, currency, description, favoritesIcon
                ) = createRefs()
                Text(
                    "COUNTRY PROFILE",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.constrainAs(profile) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
                Text(
                    countryInfo.name?.common ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.constrainAs(name) {
                        top.linkTo(profile.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.constrainAs(regionIcon) {
                        top.linkTo(name.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
                Text(
                    countryInfo.region ?: "Unknown",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.constrainAs(region) {
                        bottom.linkTo(regionIcon.bottom)
                        start.linkTo(regionIcon.end, margin = 4.dp)
                    }
                )
                FavouritesIcon(
                    toggleFavourites,
                    countryInfo.isFavourite,
                    modifier = Modifier.constrainAs(favoritesIcon) {
                        top.linkTo(name.top)
                        end.linkTo(parent.end, margin = 16.dp)
                    })
                InfoCard(
                    title = "CAPITAL",
                    content = countryInfo.capital?.getOrNull(0) ?: "",
                    modifier = Modifier.constrainAs(capital) {
                        top.linkTo(regionIcon.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(population.start, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
                )
                InfoCard(
                    title = "POPULATION",
                    content = if (countryInfo.population != null) String.format(
                        "%.1f",
                        (countryInfo.population) / 1000000f
                    ) + "M" else "Unknown",
                    modifier = Modifier.constrainAs(population) {
                        top.linkTo(regionIcon.bottom, margin = 8.dp)
                        start.linkTo(capital.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
                )
                InfoCard(
                    title = "CURRENCY",
                    content = countryInfo.currencies?.joinToString("\n") { "${it.name} (${it.symbol})" } ?: "",
                    modifier = Modifier.constrainAs(currency) {
                        top.linkTo(capital.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(population.start, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
                )
                InfoCard(
                    title = "REGION", content = countryInfo.region ?: "",
                    modifier = Modifier.constrainAs(description) {
                        top.linkTo(capital.bottom, margin = 8.dp)
                        start.linkTo(currency.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
                )
            }

        }
        CountryDescription(
            title = "About ${countryInfo.name?.common}",
            content = "some description"
        )
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
                names = NamesDto(
                    common = "Spain",
                    official = "Kingdom of Spain",
                ),
                flag = FlagDto(
                    urlPng = "https://upload.wikimedia.org/wikipedia/en/thumb/9/93/Coat_of_arms_of_Spain.svg/1200px-Coat_of_arms_of_Spain.svg.png",
                ),
                region = "Europe",
                population = 47351567,
                capitals = listOf(CapitalDto(name = "Madrid")),
                currencies = listOf(
                    CurrencyItemDto(code = "EUR", name = "Euro", symbol = "€"),
                ),
            ),
            toggleFavourites = {}
        )
    }
}