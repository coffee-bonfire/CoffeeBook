@file:OptIn(ExperimentalFoundationApi::class)

package com.dandanbiyori.coffeebooksapp.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.Book
import com.dandanbiyori.coffeebooksapp.BookViewModel
import com.dandanbiyori.coffeebooksapp.R
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    books: List<Book>,
    bookViewModel: BookViewModel,
    navController: NavController,
    pages: Array<CoffeeBooksPage> = CoffeeBooksPage.values(),
) {
    // Display 10 items
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black
                    )
                },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD3A780))
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFFD3A780),
                contentColor = Color(0xFFD3A780)
            ) {
                BottomNavigationBar(
                    navController = navController,
                    color = 0xFFD3A780
                )
            }
        },
        // :bug: TODO CoffeeBookでも出てしまう時があった
        floatingActionButton = {
            if (bookViewModel.isUserBook) {
                FloatingActionButton(
                    onClick = {
                        bookViewModel.isShowDialog = true
                    })
                {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "図鑑新規作成")
                }
            }
        }
    ) {
        // innerPadding は、Scaffold コンポーネントの中で他のコンポーネントを配置する際に、その内側の余白を表す
            innerPadding ->
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    0.dp,
                    60.dp,
                    0.dp,
                    innerPadding.calculateBottomPadding()
                )
            )
        ) {
            Column {
                // Tab Row
                TabRow(
                    selectedTabIndex = pagerState.currentPage
                ) {
                    pages.forEachIndexed { index, page ->
                        val title = stringResource(id = page.titleResId)
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(text = title) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = page.drawableResId),
                                    contentDescription = title
                                )
                            },
                            unselectedContentColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                // pages
                HorizontalPager(
                    state = pagerState,
                ) { index ->
                    when (pages[index]) {
                        CoffeeBooksPage.COFFEE_BOOKS -> {
                            bookViewModel.isUserBook = false
                            // Our page content
                            CoffeeBookList(
                                books = books,
                                onClickRow = {

                                },
                                onClickUpdate = {
                                    bookViewModel.setEditingBook(it)
                                    bookViewModel.isShowDialog = true
                                },
                                onClickDelete = {

                                },
                                navController
                            )
                        }

                        CoffeeBooksPage.USER_BOOKS -> {
                            bookViewModel.isUserBook = true
                            // Our page content
                            UserBookList(
                                books = books,
                                onClickRow = {

                                },
                                onClickUpdate = {
                                    bookViewModel.setEditingBook(it)
                                    bookViewModel.isShowDialog = true
                                },
                                onClickDelete = {
                                    bookViewModel.deleteBook(it)
                                },
                                navController
                            )
                        }
                    }


                }
            }

        }
    }

}

enum class CoffeeBooksPage(
    @StringRes val titleResId: Int,
    @DrawableRes val drawableResId: Int
) {
    COFFEE_BOOKS(R.string.tab_coffee_books, R.drawable.ic_action_swipe_coffee_bean),
    USER_BOOKS(R.string.tab_my_books, R.drawable.ic_action_swipe_user_book)
}