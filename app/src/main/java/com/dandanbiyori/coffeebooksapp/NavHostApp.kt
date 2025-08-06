package com.dandanbiyori.coffeebooksapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dandanbiyori.coffeebooksapp.components.BookDetail
import com.dandanbiyori.coffeebooksapp.components.BookEditDialog
import com.dandanbiyori.coffeebooksapp.components.BookItemDetailView
import com.dandanbiyori.coffeebooksapp.components.BookItemEditDialog
import com.dandanbiyori.coffeebooksapp.components.CoffeeItemEdit
import com.dandanbiyori.coffeebooksapp.components.PrivacyPolicyView
import com.dandanbiyori.coffeebooksapp.components.SettingComponent
import com.dandanbiyori.coffeebooksapp.ui.HomeScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavHostApp(
    navController: NavHostController,
    bookViewModel: BookViewModel = hiltViewModel(),
    bookItemViewModel: BookItemViewModel = hiltViewModel(),
) {
    Log.e("NavHostApp", "呼び出された")
    val context: Context = LocalContext.current
    var bookIdForDialog by remember { mutableStateOf(0) }
    val bookItem by bookItemViewModel.bookItem.collectAsState()

    if (bookViewModel.isShowDialog) {
        BookEditDialog(
            context,
            bookViewModel
        )
    }
    if (bookItemViewModel.isShowDialog) {
        BookItemEditDialog(
            context,
            bookIdForDialog,
            bookItemViewModel
        )
    }
    val books by bookViewModel.books.collectAsState(initial = emptyList())

    NavHost(
        navController,
        startDestination = NavigationItem.Home.route,
    ) {
        // 画面遷移をコントロール
        // 図鑑表示画面
        composable(NavigationItem.Home.route) {
            HomeScreen(books, bookViewModel, navController)
        }
        // Setting画面
        composable(NavigationItem.Setting.route) {
            SettingComponent(navController)
        }
        // 図鑑アイテム一覧表示画面
        composable(
            // 渡したい値がある場合は、routeに引数プレースホルダーを追加する
            route = "${NavigationItem.Home.route}/{isSystemCreated}/{BookId}",
            arguments = listOf(
                navArgument("isSystemCreated") {
                    type = NavType.BoolType
                    nullable = false
                },
                navArgument("BookId") {
                    type = NavType.IntType
                    nullable = false
                })
        ) { backStackEntry ->
            BookDetail(
                bookId = backStackEntry.arguments?.getInt("BookId") ?: 0,
                onClickBack = { navController.navigateUp() },
                onClickUpdate = {
                    // 図鑑アイテム編集画面に遷移
                    if (it.type == BooksItemType.SIMPLE_ITEM) {
                        bookItemViewModel.isShowDialog = true
                        bookItemViewModel.setEditingBookItem(it)
                    }
                    if (it.type == BooksItemType.COFFEE_ITEM) {
                        bookItemViewModel.setEditingCoffeeBookItem(it)
                        navController.navigate(NavigationItem.CoffeeBookEdit.route)
                    }
                },
                onClickOpenDialog = {
                    bookItemViewModel.isShowDialog = true
                },
                navController = navController,
                onClickDelete = {
                    Log.e("NavHostApp", "削除ボタンが押されました")
                    bookItemViewModel.deleteBookItem(it)
                    bookItemViewModel.deleteImage(it.imageUri)
                },
                bookItemViewModel = bookItemViewModel,
                isSystemCreated = backStackEntry.arguments?.getBoolean("isSystemCreated") ?: false,
            )
            Log.e("NavHostApp", "BookId: ${backStackEntry.arguments?.getInt("BookId")}")
            bookIdForDialog = backStackEntry.arguments?.getInt("BookId")!!
        }

        // 図鑑アイテム詳細表示画面
        composable(
            // 渡したい値がある場合は、routeに引数プレースホルダーを追加する
            route = "${NavigationItem.Home.route}/{isSystemCreated}/{BookId}/{BookItemId}",
            arguments = listOf(
                navArgument("isSystemCreated") {
                    type = NavType.BoolType
                    nullable = false
                },
                navArgument("BookId") {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument("BookItemId") {
                    type = NavType.IntType
                    nullable = false
                })
        ) { backStackEntry ->
            val bookItemId = backStackEntry.arguments?.getInt("BookItemId") ?: 0
            bookItemViewModel.setBookItem(bookItemId)

            if (bookItem != null) {
                bookItemViewModel.setEditingBookItem(bookItem!!)
                BookItemDetailView(
                    onClickBack = {
                        navController.navigateUp()
                        bookItemViewModel.resetBookItem()
                        bookItemViewModel.resetProperties()
                    },
                    onClickEdit = {
                        bookItemViewModel.setEditingBookItem(it)
                    },
                    onClickUpdate = {
                        if (it.type == BooksItemType.SIMPLE_ITEM) {
                            bookItemViewModel.isShowDialog = true
                        }
                        if (it.type == BooksItemType.COFFEE_ITEM) {
                            bookItemViewModel.setEditingCoffeeBookItem(it)
                            navController.navigate(NavigationItem.CoffeeBookEdit.route)
                        }
                    },
                    bookItem = bookItem!!,
                    bookItemViewModel,
                    isSystemCreated = backStackEntry.arguments?.getBoolean("isSystemCreated") ?: false,
                )
            }
            bookIdForDialog = backStackEntry.arguments?.getInt("BookItemId")!!
        }

        // Setting画面
        composable(NavigationItem.CoffeeBookEdit.route) {
            CoffeeItemEdit(
                onClickBack = {
                    navController.navigateUp()
                },
                bookIdForDialog,
                navController,
                bookItemViewModel,
                context
            )
        }
        composable(NavigationItem.PrivacyPolicy.route) {
            PrivacyPolicyView(
                onClickBack = {
                    navController.navigateUp()
                },
            )
        }
    }
}