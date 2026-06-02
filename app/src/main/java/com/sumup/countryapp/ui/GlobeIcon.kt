package com.sumup.countryapp.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sumup.countryapp.R
import com.sumup.countryapp.ui.theme.CountryDimens

@Composable
internal fun GlobeIcon () {
    Icon(
    painter = painterResource(R.drawable.ic_globe),
    tint = MaterialTheme.colorScheme.primary,
    contentDescription = null,
    modifier = Modifier.size(CountryDimens.globeIconSize),
    )
}