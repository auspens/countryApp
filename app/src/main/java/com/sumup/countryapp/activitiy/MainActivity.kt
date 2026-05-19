package com.sumup.countryapp.activitiy


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sumup.countryapp.R
import com.sumup.countryapp.activitiy.ui.theme.BackgroundGray
import com.sumup.countryapp.activitiy.ui.theme.MyApplicationTheme
import com.sumup.countryapp.activitiy.ui.theme.ShapeGray
import com.sumup.countryapp.activitiy.ui.theme.Typography
import com.sumup.countryapp.activitiy.ui.theme.fontGray
import com.sumup.countryapp.datamodels.CountryDTOShort
import com.sumup.countryapp.datamodels.FlagsDto
import com.sumup.countryapp.datamodels.NameDto
import com.sumup.countryapp.viewmodel.CountryViewModel
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
        topBar = {
            TopAppBar()
        },
        modifier = Modifier.padding(5.dp)
    ) { padding ->
        when (state) {
            is HomeUiState.Data -> LazyColumn(modifier = Modifier.padding(padding)) {
                items((state as HomeUiState.Data).countries) { country ->
                    CountryItem(
                        country
                    )
                }
            }
            is HomeUiState.Loading -> CircularProgressIndicator()
            else -> Text(text = "Error loading countries list")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFEBE7EC)
        ),
        windowInsets = WindowInsets(0.dp, top = 16.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xFFEBE7EC))
            )
            {
                Icon(
                    painter = painterResource(R.drawable.ic_globe),
                    tint = Color(0xFF24389C),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    "WorldAtlas", style = typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton({},
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp),
                        tint = Color(0xFF454652)
                    )
                }
            }
        },
    )
}

@Composable
private fun CountryItem(country: CountryDTOShort, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {})
            .padding(6.dp)
            .background(color = Color(0xFFF7F2F8)),
        border = BorderStroke(width = 1.dp, color = Color(0xFFE5E1E7))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f)
                .background(color = BackgroundGray)
                .padding(16.dp)
        ) {
            AsyncImage(
                model = country.flags?.png,
                contentDescription = null,
                placeholder = BrushPainter(
                    Brush.linearGradient(
                        listOf(
                            Color(color = 0xFFFFFFFF),
                            Color(color = 0xFFDDDDDD),
                        )
                    )
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(64.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, Color(0xFFC5C5D4)))
                    .background(Color.Transparent)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp)),

            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(2f)
            ) {
                Text(text = country.name?.common ?: "Unknown", style = typography.bodyLarge)
                Text(text = country.region ?: "Unknown", style = typography.bodySmall)
            }
            Icon(
                painter = painterResource(R.drawable.ic_star_outlined),
                contentDescription = null,
                tint = fontGray,
                modifier = Modifier.padding(end = 20.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CountryItemPreview() {
    val country = CountryDTOShort(
        region = "Europe",
        name = NameDto(common = "Germany", official = "Federal Republic of Germany"),
        flags = FlagsDto(png = "https://flagcdn.com/w320/de.png")
    )
    MyApplicationTheme {
        CountryItem(country)
    }
}