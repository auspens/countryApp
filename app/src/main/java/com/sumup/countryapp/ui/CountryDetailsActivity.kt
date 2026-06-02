package com.sumup.countryapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.rememberNavBackStack
import com.sumup.countryapp.navigation.Home
import com.sumup.countryapp.ui.theme.CountryAppTheme
import com.sumup.countryapp.viewmodel.CountryDirectoryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountryDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cca2:String = intent.getStringExtra("cca2") ?: ""
        val viewModel: CountryDirectoryViewModel by viewModels()
        viewModel.switchToCountryDetails(cca2)
        setContent {
            CountryAppTheme {
                Scaffold(
                    topBar = { TopBar({ArrowButton {
                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                    }}, "World Atlas", {}) })
                { padding ->
                   CountryDetailsScreen(viewModel, Modifier.padding(padding),
                       { viewModel.toggleFavoriteInDetails(cca2) },
                       retry = { viewModel.switchToCountryDetails(cca2) })
                }
            }
        }
    }
}

@Composable
private fun ArrowButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}