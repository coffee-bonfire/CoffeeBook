package com.dandanbiyori.coffeebooksapp.components


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.dandanbiyori.coffeebooksapp.BookItem
import com.dandanbiyori.coffeebooksapp.BookItemViewModel
import com.dandanbiyori.coffeebooksapp.BooksItemType
import com.dandanbiyori.coffeebooksapp.R
import com.dandanbiyori.coffeebooksapp.Util

@Composable
fun BookItemDetailView(
    onClickBack: () -> Unit,
    onClickEdit: (BookItem) -> Unit,
    bookItem: BookItem,
    bookItemViewModel: BookItemViewModel
) {
    Log.e("BookItemDetailView", "呼び出された")
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BookItemDetailContent(
            bookItem,
            onClickEdit,
            bookItemViewModel
        )
        BookItemDetailTopBar(
            onClickBack,
        )
    }

}

@Composable
fun BookItemDetailTopBar(
    onClickBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = Dimens.ToolbarIconPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconModifier = Modifier
            .sizeIn(
                maxWidth = Dimens.ToolbarIconSize,
                maxHeight = Dimens.ToolbarIconSize
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )

        IconButton(
            onClick = onClickBack,
            modifier = Modifier
                .padding(start = Dimens.ToolbarIconPadding)
                .then(iconModifier)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItemDetailContent(
    bookItem: BookItem,
    onClickEdit: (BookItem) -> Unit,
    bookItemViewModel: BookItemViewModel
) {
    Log.e("BookItemDetailContent", "呼び出された")
    val scrollState = rememberScrollState()

    var text by remember { mutableStateOf("") }
    val maxChars = 200
    val switchInputFlag = remember { mutableStateOf(false) }
    val fontFamily = FontFamily.Monospace
    text = bookItem.description

    Column(
        Modifier.verticalScroll(scrollState)
    ) {
        ConstraintLayout {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // image
                BookItemImage(bookItem)
                // title
                Spacer(modifier = Modifier.height(10.dp))
                if (bookItem.type == BooksItemType.COFFEE_ITEM) {
                    Log.e("BookItemDetailContent_Column", "呼び出された")
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                    ) {
                        Text(
                            text = bookItemViewModel.title,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = bookItemViewModel.country,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = bookItemViewModel.varieties,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = bookItemViewModel.processing,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = bookItemViewModel.flavor,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = bookItemViewModel.roast,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }


                } else {
                    Text(
                        // 10文字を超過したら省略
                        text = if (bookItem.title.length > 10) {
                            bookItem.title.substring(0, 10) + "..."
                        } else {
                            bookItem.title
                        },
                        color = Color.Gray,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(20.dp, 0.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp),
                    ) {
                        if (!switchInputFlag.value) {
                            Log.e("text.description", "呼び出された")
                            Text(
                                text = bookItem.description,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),// 高さを指定
                                maxLines = 10 // 最大行数を指定
                            )
                        } else {
                            Log.e("TextField.description", "呼び出された")
                            TextField(
                                value = text,
                                onValueChange = {
                                    // 200文字を超えないようにする
                                    if (it.length <= maxChars) {
                                        text = it
                                        bookItemViewModel.description = it
                                    }
                                },
                                label = { Text("Description for Item") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp), // 高さを指定
                                maxLines = 10, // 最大行数を指定
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 20.sp, fontFamily = fontFamily
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween, // 左寄せと右寄せにする
                                verticalAlignment = Alignment.CenterVertically // 垂直方向中央揃え
                            ) {
                                // 左側に追加したいテキスト
                                Text(
                                    text = if (text.length > maxChars * 0.9) "Plese enter 200 characters or less." else "",
                                    color = Color.Red // 必要に応じて色を設定
                                )

                                // 元のテキスト（右寄せ）
                                Text(
                                    text = "${text.length}/$maxChars",
                                    color = if (text.length == maxChars) Color.Red else Color.Gray,
                                    modifier = if (text.length > maxChars * 0.9) Modifier else Modifier.weight(
                                        1f
                                    )
                                )
                            }
                        }

                        Row {
                            Spacer(modifier = Modifier.weight(1f)) // 空白を挿入
                            if (!switchInputFlag.value) {
                                Button(
                                    onClick = {
                                        Log.e("Button.book", bookItem.title)
                                        switchInputFlag.value = true
                                        onClickEdit(bookItem)
                                    },
                                ) {
                                    Text("Edit")
                                }
                            } else {
                                Button(
                                    onClick = {
                                        // ユーザ入力値でDB更新
                                        bookItemViewModel.updateBookItem()
                                        text = bookItem.description
                                        switchInputFlag.value = false
                                    },
                                ) {
                                    Text("Save")
                                }
                                Button(
                                    onClick = {
                                        switchInputFlag.value = false
                                        text = bookItem.description
                                    },
                                ) {
                                    Text("Back")
                                }
                            }

                        }

                    }

                }

            }

        }
    }
}

@Composable
private fun BookItemImage(
    bookItem: BookItem?
) {
    var imageBitmap: Bitmap? = null
    val context = LocalContext.current
    val uri: Uri? = bookItem?.let { Util.convertStringToUri(it.imageUri) }

    // uriをBitmapに変換
    if (uri != null && uri.toString().isNotEmpty()) {
        imageBitmap = Util.convertUriToBitmap(uri, context)
    }

    // 変換できなかった場合や画像が存在しない場合はデフォルトの画像を使用する
    if (imageBitmap == null) {
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.default_icon)
        imageBitmap = drawable?.toBitmap()
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            bitmap = imageBitmap!!.asImageBitmap(),
            contentDescription = "Book Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}