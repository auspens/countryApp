package com.sumup.countryapp.activitiy


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumup.countryapp.activitiy.ui.theme.MyApplicationTheme
import com.sumup.countryapp.viewmodel.CountryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Shapes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumup.countryapp.datamodels.CountryDTOShort
import com.sumup.countryapp.viewmodel.HomeUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CountryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                CountriesList(viewModel)
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountriesList(viewModel: CountryViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Countries") }) },
        modifier = Modifier.padding(4.dp)
    ) { _ ->
        when (state) {
            is HomeUiState.Data -> LazyColumn(modifier = Modifier.padding(20.dp)) {
                items((state as HomeUiState.Data).countries) { country ->
                    CountryItem(
                        country,
                        viewModel
                    )
                }
            }

            is HomeUiState.Loading -> CircularProgressIndicator()
            else -> Text(text = "Error loading countries list")


        }

    }
}

@Composable
private fun CountryItem(country: CountryDTOShort, viewModel: CountryViewModel) {
   Box(modifier = Modifier
       .clip(RoundedCornerShape(5))
//       TODO: add color background
     ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            AsyncImage(
                model = country.flags?.png,
                contentDescription = null,
                modifier = Modifier.clip(shape = RoundedCornerShape(5))
            )
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = country.name?.common ?: "Unknown", fontSize = 24.sp)
                Text(text = country.region ?: "Unknown")
            }
        }
    }

}

