package com.sumup.countryapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.countryCard(onClick: () -> Unit = {}): Modifier = this
    .fillMaxWidth()
    .clickable(onClick = onClick)
    .padding(CountryDimens.cardPadding)

@Composable
fun Modifier.countryCardRow(): Modifier = this
    .fillMaxWidth()
    .aspectRatio(CountryDimens.cardRowAspectRatio)
    .background(MaterialTheme.colorScheme.surfaceVariant)
    .padding(CountryDimens.contentPadding)

@Composable
fun Modifier.topBarTitleRow(): Modifier = this
    .fillMaxWidth()
    .background(MaterialTheme.colorScheme.background)

@Composable
fun Modifier.flagImage(): Modifier {
    val shape = RoundedCornerShape(CountryDimens.cornerRadius)
    return this
        .width(CountryDimens.flagWidth)
        .height(CountryDimens.flagHeight)
        .clip(shape)
        .border(
            BorderStroke(CountryDimens.borderWidth, flagBorderColor()),
            shape,
        )
        .background(androidx.compose.ui.graphics.Color.Transparent)
        .shadow(
            elevation = CountryDimens.flagShadowElevation,
            shape = shape,
        )
}

fun Modifier.skeletonBox(width: Dp, height: Dp): Modifier = this
    .width(width)
    .height(height)
    .clip(RoundedCornerShape(CountryDimens.cornerRadius))
