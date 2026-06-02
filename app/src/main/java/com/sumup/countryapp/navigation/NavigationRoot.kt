package com.sumup.countryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sumup.countryapp.ui.CountryDetailsScreen
import com.sumup.countryapp.ui.CountryDirectoryScreen
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel

@Composable
fun NavigationRoot(
    backStack: NavBackStack<NavKey>,
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier = Modifier

) {
    var cca2 = ""
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                viewModel.switchToAll()
                CountryDirectoryScreen(
                    viewModel, modifier,
                    onChooseCountryClick = {
                        cca2 = it
                        backStack.add(CountryDetails)
                    })
            }
            entry<Saved> {
                viewModel.switchToFavourites()
                CountryDirectoryScreen(
                    viewModel, modifier, onShowAllCountriesClick = {
                        backStack.add(Home)
                    },
                    onChooseCountryClick = {
                        cca2 = it
                        backStack.add(CountryDetails)
                    })
            }
            entry<CountryDetails> {
                viewModel.switchToCountryDetails(cca2)
                CountryDetailsScreen(viewModel, modifier,
                    toggleFavourites = {
                    viewModel.toggleFavoriteInDetails(
                        cca2
                    )
                })
            }
        }
    )
}