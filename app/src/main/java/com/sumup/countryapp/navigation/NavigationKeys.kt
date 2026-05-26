package com.sumup.countryapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


interface BottomNavItem {
 val icon: ImageVector
 val title: String
}


@Serializable
data object Home:  NavKey, BottomNavItem {
 override val icon: ImageVector
  get() = Icons.Filled.Home
 override val title: String
  get() = "Home"
}

@Serializable
data object Favourites: NavKey, BottomNavItem {
     override val icon: ImageVector
         get() = Icons.Filled.Star
     override val title: String
         get() = "Favourites"
 }