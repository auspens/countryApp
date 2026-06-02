package com.sumup.countryapp.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sumup.countryapp.ui.CountryDetailsActivity
import com.sumup.countryapp.ui.CountryDetailsScreen
import com.sumup.countryapp.ui.CountryDirectoryScreen
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import android.content.Context
import androidx.compose.ui.platform.LocalContext

@Composable
fun NavigationRoot(
    backStack: NavBackStack<NavKey>,
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current
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
                val countryScreenIntent = Intent(context, CountryDetailsActivity::class.java)
                    .putExtra("cca2", cca2)
                context.startActivity(countryScreenIntent)
            }
        }
    )
}