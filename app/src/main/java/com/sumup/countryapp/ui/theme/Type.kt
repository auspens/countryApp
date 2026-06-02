package com.sumup.countryapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.sumup.countryapp.R

val GoogleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

val FontFamilyRoboto = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto Flex"),
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Bold,
    ),
)

val FontFamilyNoto = FontFamily(
    Font(
        googleFont = GoogleFont("Noto Sans"),
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Bold,
    ),
)

val CountryTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamilyRoboto,
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
        color = OnSurface,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamilyRoboto,
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        color = OnSurfaceMuted,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamilyNoto,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        color = BrandPrimary,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamilyRoboto,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = OnSurfaceDim,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamilyNoto,
        fontSize = 40.sp,
        lineHeight = 44.sp,
        color = OnSurface,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
    ),
)
