package com.sumup.countryapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.navigation3.runtime.rememberNavBackStack
import com.sumup.countryapp.R
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.datamodels.CodesDto
import com.sumup.countryapp.datamodels.FlagDto
import com.sumup.countryapp.datamodels.NamesDto
import com.sumup.countryapp.navigation.Home
import com.sumup.countryapp.navigation.NavigationRoot
import com.sumup.countryapp.navigation.Saved
import com.sumup.countryapp.navigation.navigateToTab
import com.sumup.countryapp.ui.theme.CountryAppTheme
import com.sumup.countryapp.ui.theme.CountryDimens
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
                val bottomNavItems = listOf(Home, Saved)
                val backStack = rememberNavBackStack(Home)
                Scaffold(
                    topBar = {
                        TopBar(
                            icon = { GlobeIcon() },
                            clickAction = {},
                            text = when (backStack.lastOrNull()) {
                                Saved -> "Saved"
                                else -> "World Atlas"
                            },
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            bottomNavItems.forEach { item ->
                                val selected = backStack.lastOrNull() == item
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        if (!selected) {
                                            navigateToTab(backStack, item, viewModel)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title
                                        )
                                    },
                                    label = {
                                        Text(item.title)
                                    },
                                )
                            }
                        }
                    })
                { padding ->
                    NavigationRoot(
                        backStack = backStack,
                        viewModel = viewModel,
                        modifier = Modifier.padding(padding),
                        onNavigateToHome = { navigateToTab(backStack, Home, viewModel) },
                    )
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDirectoryScreen(
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier = Modifier,
    isSavedScreen: Boolean = false,
    onShowAllCountriesClick: () -> Unit = {},
    onChooseCountryClick: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        is HomeUiState.Data ->
            CountriesList(
                state = state as HomeUiState.Data,
                modifier = modifier,
                isSavedScreen = isSavedScreen,
                onShowAllCountriesClick = onShowAllCountriesClick,
                onToggleFavourite = viewModel::toggleFavorite,
                onApplyFilter = viewModel::applyFilter,
                onChooseCountryClick = onChooseCountryClick,
                onRetry = { viewModel.fetchCountriesAndRegions() },
            )

        is HomeUiState.Loading -> LoadingPage()

        else -> ErrorPage(
            modifier = Modifier.padding(6.dp),
            retry = { viewModel.fetchCountriesAndRegions() },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryTopAppBar(clickAction: () -> Unit, text: String) {
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
                    text = text,
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

@Preview(showBackground = true)
@Composable
fun CountryItemPreview() {
    val country = CountryBasic(
        codes = CodesDto(alpha2 = "DE"),
        names = NamesDto(
            common = "Germany",
            official = "Federal Republic of Germany",
        ),
        flag = FlagDto(urlPng = "https://flagcdn.com/w320/de.png"),
        region = "Europe",
    )
    CountryAppTheme {
        CountryItem(
            country = country,
            modifier = Modifier,
            clickAction = {},
            toggleFavorites = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    CountryAppTheme {
        ErrorPage(modifier = Modifier, retry = {})
    }
}

@Preview(showBackground = true)
@Composable
fun RegionChipPreview() {
    CountryAppTheme() {
        RegionFilterChip("Europe", "Europe") {}
    }
}
