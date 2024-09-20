package com.dandanbiyori.coffeebooksapp.components

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
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
    bookItems : List<BookItem>,
    navController : NavController,
    onClickOpenDialog: () -> Unit,
    onClickDelete: (BookItem) -> Unit,
    bookItemViewModel: BookItemViewModel = hiltViewModel(),
){
    Log.e("BookDetail", "呼び出された")

    val targetBookItems = mutableListOf<BookItem>()

    bookItems.forEach{ bookItem ->
        if( bookItem.bookId == bookId){
            targetBookItems.add(bookItem)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD3A780))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onClickOpenDialog()
                },
                contentColor = Color(0xFFD3A780),
                containerColor = Color(0xFFD3A780),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "図鑑アイテム作成・更新",
                    tint = Color.Black
                )}
        }

    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.card_side_margin))
            ){
                items(
                    count = targetBookItems.size,
                    key = {index ->  index},
                    itemContent = { index ->
                        // BUG デフォルトの画像が表示される事象がある
                        BookDeteilScreen(
                            targetBookItems[index],
                            bookId,
                            navController,
                            onClickUpdate,
                            onClickDelete
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
fun BookDeteilScreen (
    bookItem: BookItem,
    bookId: Int,
    navController: NavController,
    onClickUpdate: (BookItem) -> Unit,
    onClickDelete: (BookItem) -> Unit,
    bookItemViewModel: BookItemViewModel = hiltViewModel(),
){
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
    if (imageBitmap == null){
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.default_icon)
        imageBitmap = drawable?.toBitmap()
    }

    Card(
        onClick = {
            navController.navigate("${NavigationItem.Home.route}/${bookId}/${bookItem.id}")
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
            .combinedClickable(
                onClick = {},
                onLongClick = {},
                onClickLabel = "画面表示"
            ),
    ) {
        Column(Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()){
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = "bookdetail",
                    Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.book_item_image_height)),
                    contentScale = ContentScale.Crop
                )
                Row( // Use Row for horizontal layout
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
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
                            onClick = {
                                expanded = false
                                selectedOption = "delete"
                                onClickDelete(bookItem)
                                Log.e("onClickDelete", "呼び出された")
                            }
                        )
                    }
                }
            }
            Text(
                // 10文字を超過したら省略
                text = if (bookItem.title.length > 10){
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