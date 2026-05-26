package com.sumup.countryapp.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel

@Composable
fun SavedScreen(viewModel: CountryDirectoryViewModel, modifier: Modifier) {
        viewModel.switchToFavourites()
        CountryDirectoryScreen(viewModel = viewModel, modifier)
}
