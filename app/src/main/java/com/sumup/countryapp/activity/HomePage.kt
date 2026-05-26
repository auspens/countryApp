package com.sumup.countryapp.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel

@Composable
fun HomePage(viewModel: CountryDirectoryViewModel, modifier: Modifier = Modifier){
    viewModel.switchToAll()
        CountryDirectoryScreen(viewModel = viewModel, modifier)
}