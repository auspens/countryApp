package com.sumup.countryapp.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.sumup.countryapp.R
import com.sumup.countryapp.datamodels.CountryBasic
import com.sumup.countryapp.ui.theme.CountryDimens
import com.sumup.countryapp.ui.theme.FlagPlaceholderEnd
import com.sumup.countryapp.ui.theme.FlagPlaceholderStart
import com.sumup.countryapp.ui.theme.countryCard
import com.sumup.countryapp.ui.theme.countryCardRow
import com.sumup.countryapp.ui.theme.flagImage

@Composable
internal fun CountryItem(
    country: CountryBasic,
    modifier: Modifier = Modifier,
    clickAction: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.countryCard(onClick = clickAction),
        border = BorderStroke(
            width = CountryDimens.borderWidth,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion.countryCardRow(),
        ) {
            AsyncImage(
                model = country.flags?.png,
                contentDescription = null,
                placeholder = BrushPainter(
                    Brush.Companion.linearGradient(
                        listOf(FlagPlaceholderStart, FlagPlaceholderEnd),
                    ),
                ),
                contentScale = ContentScale.Companion.Crop,
                modifier = Modifier.Companion.flagImage(),
            )
            Column(
                modifier = Modifier.Companion
                    .padding(horizontal = CountryDimens.columnHorizontalPadding)
                    .weight(2f),
            ) {
                Text(
                    text = country.name?.common ?: "Unknown",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = country.region ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )            }
            Icon(
                painter = painterResource(R.drawable.ic_star_outlined),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.Companion.padding(end = CountryDimens.starIconEndPadding),
            )
        }
    }
}
