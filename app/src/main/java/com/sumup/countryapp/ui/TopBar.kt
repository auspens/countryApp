package com.sumup.countryapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sumup.countryapp.R
import com.sumup.countryapp.ui.theme.CountryDimens
import com.sumup.countryapp.ui.theme.countryTopAppBarColors
import com.sumup.countryapp.ui.theme.topBarTitleRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (icon: @Composable () -> Unit, text: String, clickAction: () -> Unit) {
    TopAppBar(
        colors = countryTopAppBarColors(),
        windowInsets = WindowInsets(
            left = CountryDimens.scaffoldOuterPadding,
            top = CountryDimens.topBarTopInset,
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.topBarTitleRow(),
            ) {
                icon()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = CountryDimens.topBarIconSpacing),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = clickAction,
                    modifier = Modifier.padding(CountryDimens.topBarActionPadding),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier.size(CountryDimens.searchIconSize),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
    )
}