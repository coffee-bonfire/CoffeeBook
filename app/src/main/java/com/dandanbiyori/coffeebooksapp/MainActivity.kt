package com.dandanbiyori.coffeebooksapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dandanbiyori.coffeebooksapp.components.BookDetail
import com.dandanbiyori.coffeebooksapp.components.BookEditDialog
import com.dandanbiyori.coffeebooksapp.components.BookItemEditDialog
import com.dandanbiyori.coffeebooksapp.components.HomeScreen
import com.dandanbiyori.coffeebooksapp.components.SettingComponent
import com.dandanbiyori.coffeebooksapp.ui.theme.CoffeeBooksAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CoffeeBooksAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(navController = navController)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    bookViewModel: BookViewModel = hiltViewModel(),
    bookItemViewModel: BookItemViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val context = LocalContext.current
    // TopAppBar のスクロール動作を指定するためのデフォルトの設定
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var bookIdForDialog by remember { mutableStateOf(0) }


    if (bookViewModel.isShowDialog) {
        BookEditDialog(context)
    }
    if (bookItemViewModel.isShowDialog) {
        BookItemEditDialog(
            context,
            bookIdForDialog
        )
    }
    var showFab by remember { mutableStateOf(true) }
    val books by bookViewModel.books.collectAsState(initial = emptyList())
    val bookItems by bookItemViewModel.bookItems.collectAsState(initial = emptyList())

    NavHost(
        navController,
        startDestination = NavigationItem.Home.route,
    ) {
        // 画面遷移をコントロール
        // 図鑑表示画面
        composable(NavigationItem.Home.route) {
            HomeScreen(books, bookViewModel,navController)
        }
        // Setting画面
        composable(NavigationItem.Setting.route) {
            SettingComponent(navController)
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
            BookDetail(
                bookId = backStackEntry.arguments?.getInt("BookId") ?: 0,
                onClickBack = {navController.navigateUp()},
                onClickUpdate = { bookItemViewModel.isShowDialog = true},
                bookItems = bookItems
            )
            bookIdForDialog = backStackEntry.arguments?.getInt("BookId")!!
        }
    }


}

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    object Home : NavigationItem("Home", Icons.Default.Home, "Home")
    object Setting : NavigationItem("setting", Icons.Default.Settings, "Setting")
    object Book : NavigationItem("Book", Icons.Default.Home, "Book")
}