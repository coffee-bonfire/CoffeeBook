package com.dandanbiyori.coffeebooksapp.components

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.BookItem
import com.dandanbiyori.coffeebooksapp.BookItemViewModel
import com.dandanbiyori.coffeebooksapp.NavigationItem
import com.dandanbiyori.coffeebooksapp.R
import com.dandanbiyori.coffeebooksapp.Util

// Book詳細画面（BookItem一覧）
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetail(
    bookId: Int,
    onClickBack: () -> Unit,
    onClickUpdate: (BookItem) -> Unit,
    navController: NavController,
    onClickOpenDialog: (BookItemViewModel) -> Unit,
    onClickDelete: (BookItem) -> Unit,
    bookItemViewModel: BookItemViewModel,
) {
    Log.e("BookDetail", "呼び出された")

    val targetBookItems = mutableListOf<BookItem>()
    val bookItems by bookItemViewModel.bookItemsByBookId.collectAsState()
    // LaunchedEffect を使用して getBookItemsByBookId を呼び出す
    LaunchedEffect(key1 = bookId) {
        bookItemViewModel.setBookItemsByBookId(bookId)
    }

    bookItems.forEach { bookItem ->
        targetBookItems.add(bookItem)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(NavigationItem.Home.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD3A780)),
                // ver 1.0以降で対応する TODO
//                actions = {
//                    // BookItemのソート機能
//                    var expanded by remember { mutableStateOf(false) }
//                    var selectedOption by remember { mutableStateOf<String?>(null) }
//                    IconButton(onClick = {
//                        expanded = true
//                    }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "sort book"
//                        )
//                    }
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false },
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Date") },
//                            onClick = {
//                                expanded = false
//                                selectedOption = "sort date"
//                            }
//                        )
//                        DropdownMenuItem(
//                            text = { Text("Item Name") },
//                            onClick = {
//                                expanded = false
//                                selectedOption = "sort name"
//                                Log.e("onClickDelete", "呼び出された")
//                            }
//                        )
//                    }
//                }
            )
        },
        floatingActionButton = {
            val simpleItem = "Simple Item"
            val coffeeItem = "Coffee Item"
            var expanded by remember { mutableStateOf(false) }
            val items = listOf(
                MiniFabItems(Icons.Filled.Create, simpleItem),
                MiniFabItems(
                    ImageVector.vectorResource(id = R.drawable.ic_action_button_coffee),
                    coffeeItem
                ),
            )
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
                ) {
                    LazyColumn(Modifier.padding(bottom = 8.dp)) {
                        items(items.size) {
                            ItemUi(
                                icon = items[it].icon,
                                title = items[it].title,
                                onClickOpenDialog = onClickOpenDialog,
                                navController = navController,
                                bookItemViewModel
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                val transition = updateTransition(targetState = expanded, label = "transition")
                val rotation by transition.animateFloat(label = "rotation") {
                    if (it) 315f else 0f
                }

                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = Color(0xFFD3A780)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add, contentDescription = "",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.card_side_margin))
            ) {
                items(
                    count = targetBookItems.size,
                    key = { index -> index },
                    itemContent = { index ->
                        // BUG デフォルトの画像が表示される事象がある
                        BookDeteilScreen(
                            targetBookItems[index],
                            bookId,
                            navController,
                            onClickUpdate,
                            onClickDelete,
                            onClickUpdateBookItem = {
                                bookItemViewModel.setBookItemsByBookId(it)
                            }
                        )
                    }
                )
            }
        }
    }
}

// 各BookItemのCard
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookDeteilScreen(
    bookItem: BookItem,
    bookId: Int,
    navController: NavController,
    onClickUpdate: (BookItem) -> Unit,
    onClickDelete: (BookItem) -> Unit,
    onClickUpdateBookItem: (Int) -> Unit,
) {
    Log.e("BookDeteilScreen", "呼び出された")
    var imageBitmap: Bitmap? = null
    val context = LocalContext.current
    val uri: Uri? = bookItem.let { Util.convertStringToUri(it.imageUri) }

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    // uriをBitmapに変換
    if (uri != null && uri.toString().isNotEmpty()) {
        imageBitmap = Util.convertUriToBitmap(uri, context)
    }

    // 変換できなかった場合や画像が存在しない場合はデフォルトの画像を使用する
    if (imageBitmap == null) {
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.default_icon)
        imageBitmap = drawable?.toBitmap()
    }

    Card(
        onClick = {
            navController.navigate("${NavigationItem.Home.route}/${bookId}/${bookItem.id}")
        },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3A780)),
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
    ) {
        Column(Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = "bookdetail",
                    Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.book_item_image_height)),
                    contentScale = ContentScale.Crop
                )
                Row( // Use Row for horizontal layout
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End // Align to the end (right)
                ) {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(24.dp) // Maintain size
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Book edit")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                expanded = false
                                selectedOption = "edit"
                                onClickUpdate(bookItem)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            // 削除されないことがあるため用修正　TODO
                            onClick = {
                                expanded = false
                                selectedOption = "delete"
                                onClickDelete(bookItem)
                                onClickUpdateBookItem(bookId)
                                Log.e("onClickDelete", "呼び出された")
                            }
                        )
                    }
                }
            }
            Text(
                // 10文字を超過したら省略
                text = if (bookItem.title.length > 10) {
                    bookItem.title.substring(0, 10) + "..."
                } else {
                    bookItem.title
                },
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.margin_normal))
                    .wrapContentWidth(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun ItemUi(
    icon: ImageVector,
    title: String,
    onClickOpenDialog: (BookItemViewModel) -> Unit,
    navController: NavController,
    bookItemViewModel:BookItemViewModel,
) {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Text(text = title)
        }
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(onClick = {
            when (title) {
                "Simple Item" -> {
                    onClickOpenDialog(bookItemViewModel)
                }

                "Coffee Item" -> {
                    navController.navigate(NavigationItem.CoffeeBookEdit.route)
                }
            }
        }, modifier = Modifier.size(45.dp), containerColor = Color(0xFFD3A780)) {
            Icon(imageVector = icon, contentDescription = "")
        }
    }
}

data class MiniFabItems(
    val icon: ImageVector,
    val title: String
)