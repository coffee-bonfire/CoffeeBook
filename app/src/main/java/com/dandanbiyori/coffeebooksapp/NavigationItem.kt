package com.dandanbiyori.coffeebooksapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    object Home : NavigationItem("Home", Icons.Default.Home, "Home")
    object Setting : NavigationItem("History", Icons.Default.Settings, "Setting")
    object Book : NavigationItem("Book", Icons.Default.Home, "Book")
    object CoffeeBookEdit : NavigationItem("CoffeeBookEdit", Icons.Default.Home, "CoffeeBookEdit")
    object PrivacyPolicy : NavigationItem("PrivacyPolicy", Icons.Default.Home, "PrivacyPolicy")
}