package com.sumup.countryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sumup.countryapp.activity.SavedScreen
import com.sumup.countryapp.activity.HomePage
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel

@Composable
fun NavigationRoot(backStack: NavBackStack<NavKey>,
                   viewModel: CountryDirectoryViewModel,
                   modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                HomePage(viewModel = viewModel, modifier)
            }
            entry<Saved> {
                SavedScreen(viewModel, modifier)
            }
        }
    )
}