package com.sumup.countryapp.activitiy.ui.theme

import com.sumup.countryapp.R
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)


val fontFamilyRoboto = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto Flex"),
        fontProvider = provider,
        weight = FontWeight.Bold,
    )
)

val fontFamilyNoto = FontFamily(
    Font(
        googleFont = GoogleFont("Noto Sans"),
        fontProvider = provider,
        weight = FontWeight.Bold,
    )
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamilyRoboto,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = fontGray
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamilyNoto,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        color = Color(0xFF24389C),
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight(700)
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamilyRoboto,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = fontDarkGray
    )
)