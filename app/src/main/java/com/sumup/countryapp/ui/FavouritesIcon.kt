package com.sumup.countryapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.sumup.countryapp.R
import com.sumup.countryapp.ui.theme.CountryDimens


@Composable
internal fun FavouritesIcon(
    toggleFavorites: () -> Unit,
    isFavourite: Boolean,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = toggleFavorites,
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.padding(end = CountryDimens.starIconEndPadding),
            painter = if (isFavourite) rememberVectorPainter(Icons.Filled.Star) else painterResource(
                R.drawable.ic_star_outlined
            ),
            contentDescription = null,
            tint = if (isFavourite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
