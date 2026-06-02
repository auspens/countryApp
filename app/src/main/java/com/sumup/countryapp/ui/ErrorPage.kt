package com.sumup.countryapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sumup.countryapp.R
import com.sumup.countryapp.ui.theme.CountryDimens
import com.sumup.countryapp.ui.theme.countryButtonColors

@Composable
internal fun ErrorPage(modifier: Modifier, retry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CountryDimens.errorSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(CountryDimens.contentPadding)
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = null,
            modifier = Modifier.aspectRatio(1f),
        )
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "We couldn't load the country list.\n" +
                    "Please check your connection and\n" +
                    "try again.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(
            onClick = retry,
            modifier = Modifier.defaultMinSize(minWidth = CountryDimens.retryButtonMinWidth),
            colors = countryButtonColors(),
            content = {
                Icon(Icons.Outlined.Refresh, contentDescription = null)
                Text(
                    text = "Retry",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            },
        )
    }
}
