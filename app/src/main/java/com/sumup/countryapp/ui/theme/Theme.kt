package com.sumup.countryapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = OnPrimary,
    background = TopBarBackground,
    surface = SurfaceCard,
    surfaceVariant = SurfaceRow,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceMuted,
    outline = BorderDefault,
    tertiary = SelectedOrange,
    onTertiary = BorderFlag,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryDark,
    onPrimary = OnSurfaceDark,
    background = TopBarBackgroundDark,
    surface = SurfaceCardDark,
    surfaceVariant = SurfaceRowDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceMutedDark,
    outline = BorderDefaultDark,
    tertiary = SelectedOrange,
    onTertiary = BorderFlagDark,
)

@Composable
fun CountryAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CountryTypography,
        content = content,
    )
}



@Composable
fun countryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
)



@Composable
fun countryTopAppBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.background,
)

@Composable
fun flagBorderColor(): Color = MaterialTheme.colorScheme.secondary
