package com.sumup.countryapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumup.countryapp.R
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.FlagsDto
import com.sumup.countryapp.datamodels.NameDto
import com.sumup.countryapp.ui.theme.CountryAppTheme
import com.sumup.countryapp.ui.theme.CountryDimens
import com.sumup.countryapp.ui.theme.countryButtonColors
import com.sumup.countryapp.ui.theme.countryTopAppBarColors
import com.sumup.countryapp.ui.theme.topBarTitleRow
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import com.sumup.countryapp.viewmodel.HomeUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CountryDirectoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CountryAppTheme {
                Content(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(viewModel: CountryDirectoryViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { CountryTopAppBar(){} },
        modifier = Modifier.padding(CountryDimens.scaffoldOuterPadding),
    ) { padding ->
        when (state) {
            is HomeUiState.Data -> LazyColumn(modifier = Modifier.padding(padding)) {
                items(
                    items = (state as HomeUiState.Data).countries,
                    key = { item -> item.hashCode() },
                ) { country ->
                    CountryItem(country = country, modifier = Modifier, clickAction = {})
                }
            }

            is HomeUiState.Loading -> Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(125.dp))
                CircularProgressIndicator()
                Text("Loading...")
            }

            else -> ErrorScreen(
                modifier = Modifier.padding(padding),
                retry = { viewModel.retryFetch() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryTopAppBar(clickAction:()->Unit) {
    TopAppBar(
        colors = countryTopAppBarColors(),
        windowInsets = WindowInsets(
            left = CountryDimens.scaffoldOuterPadding,
            top = CountryDimens.topBarTopInset,
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.topBarTitleRow(),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_globe),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.size(CountryDimens.globeIconSize),
                )
                Text(
                    text = "WorldAtlas",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = CountryDimens.topBarIconSpacing),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = clickAction,
                    modifier = Modifier.padding(CountryDimens.topBarActionPadding),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier.size(CountryDimens.searchIconSize),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
    )
}

@Composable
private fun ErrorScreen(modifier: Modifier, retry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CountryDimens.errorSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(CountryDimens.contentPadding)
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = null,
            modifier = Modifier.aspectRatio(1f),
        )
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "We couldn't load the country list.\n" +
                    "Please check your connection and\n" +
                    "try again.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(
            onClick = retry,
            modifier = Modifier.defaultMinSize(minWidth = CountryDimens.retryButtonMinWidth),
            colors = countryButtonColors(),
            content = {
                Icon(Icons.Outlined.Refresh, contentDescription = null)
                Text(
                    text = "Retry",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CountryItemPreview() {
    val country = CountryBasic(
        region = "Europe",
        name = NameDto(common = "Germany", official = "Federal Republic of Germany"),
        flags = FlagsDto(png = "https://flagcdn.com/w320/de.png"),
    )
    CountryAppTheme {
        CountryItem(country = country, modifier = Modifier, clickAction = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    CountryAppTheme {
        ErrorScreen(modifier = Modifier, retry = {})
    }
}
