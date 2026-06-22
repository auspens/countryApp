package com.sumup.countryapp.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sumup.countryapp.ui.CountryDetailsActivity
import com.sumup.countryapp.ui.CountryDirectoryScreen
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import android.content.Context
import androidx.compose.ui.platform.LocalContext

@Composable
fun NavigationRoot(
    backStack: NavBackStack<NavKey>,
    viewModel: CountryDirectoryViewModel,
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                CountryDirectoryScreen(
                    viewModel = viewModel,
                    modifier = modifier,
                    isSavedScreen = false,
                    onChooseCountryClick = { countryCode ->
                        onChooseCountryClick(countryCode, context)
                    },
                )
            }
            entry<Saved> {
                CountryDirectoryScreen(
                    viewModel = viewModel,
                    modifier = modifier,
                    isSavedScreen = true,
                    onShowAllCountriesClick = onNavigateToHome,
                    onChooseCountryClick = { countryCode ->
                        onChooseCountryClick(countryCode, context)
                    },
                )
            }
        },
    )
}

fun navigateToTab(
    backStack: NavBackStack<NavKey>,
    destination: NavKey,
    viewModel: CountryDirectoryViewModel,
) {
    while (backStack.size > 1) {
        backStack.removeLastOrNull()
    }
    if (backStack.lastOrNull() != destination) {
        backStack.add(destination)
    }
    when (destination) {
        Home -> viewModel.switchToAll()
        Saved -> viewModel.switchToFavourites()
    }
}

fun onChooseCountryClick(countryCode: String, context: Context) {
    val countryScreenIntent = Intent(context, CountryDetailsActivity::class.java)
        .putExtra("countryCode", countryCode)
    context.startActivity(countryScreenIntent)
}
