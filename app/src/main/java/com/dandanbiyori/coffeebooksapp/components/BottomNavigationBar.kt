package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dandanbiyori.coffeebooksapp.BookViewModel

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Setting,
    )
    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon!!, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    object Home : NavigationItem("Home", Icons.Default.Home, "Home")
    object Setting : NavigationItem("History", Icons.Default.Settings, "Setting")
    object Book : NavigationItem("Book", Icons.Default.Home, "Book")
}

@Composable
fun Navigations(
    bookViewModel: BookViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val books by bookViewModel.books.collectAsState(initial = emptyList())

    NavHost(
        navController,
        startDestination = NavigationItem.Home.route,
        modifier = Modifier.padding(30.dp)
    ) {
        // 画面遷移をコントロール
        // 図鑑表示画面
        composable(NavigationItem.Home.route) {
            HomeScreen(books, bookViewModel,navController)
        }
        // Setting画面
        composable(NavigationItem.Setting.route) {
            SettingComponent()
        }
        // 図鑑アイテム表示画面
        composable(
            // 渡したい値がある場合は、routeに引数プレースホルダーを追加する
            route = "${NavigationItem.Home.route}/{BookId}",
            arguments = listOf(
                    navArgument("BookId") {
                        type = NavType.IntType
                        nullable = false
                    })
        ) { backStackEntry ->
            BookDeteil( bookId = backStackEntry.arguments?.getInt("BookId") ?: 1 ){
                navController.navigateUp()
            }
//            ImageListItem(
//                onBackClick = { navController.navigateUp() },
//            )
        }
        // TODO 図鑑アイテム詳細表示画面
    }
}
