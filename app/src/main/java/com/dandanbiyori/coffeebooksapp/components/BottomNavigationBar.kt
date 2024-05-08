package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dandanbiyori.coffeebooksapp.NavigationItem

@Composable
fun BottomNavigationBar(
    navController: NavController,
    color: Long
) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Setting,
    )
    var selectedItem by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 現在のルートが変更されるたびに、selectedItemとcurrentRouteが適切に更新する
    val currentRoute = navBackStackEntry?.destination?.route ?: NavigationItem.Home.route

    items.forEachIndexed { index, navigationItem ->
        val isSelected = navigationItem.route == currentRoute
        if (isSelected) {
            selectedItem = index
        }
    }

    NavigationBar(
        contentColor = Color(color),
        containerColor = Color(color),
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon!!, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
