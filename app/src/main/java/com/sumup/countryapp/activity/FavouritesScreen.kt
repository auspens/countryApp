package com.sumup.countryapp.activity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel

@Composable
fun FavouritesScreen(viewModel: CountryDirectoryViewModel) {
        viewModel.switchToFavourites()
        CountryDirectoryScreen(viewModel = viewModel)
}