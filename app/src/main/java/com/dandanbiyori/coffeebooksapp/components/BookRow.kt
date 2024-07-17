package com.dandanbiyori.coffeebooksapp.components

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.components.Util.Companion.convertStringToUri
import com.dandanbiyori.coffeebooksapp.components.Util.Companion.convertUriToBitmap
import com.dandanbiyori.coffeebooksapp.Book
import com.dandanbiyori.coffeebooksapp.NavigationItem
import com.dandanbiyori.coffeebooksapp.R
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

// 図鑑アイテム表示用Card
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookRow(
    book: (Book),
    onCllickRow: (Book) -> Unit,
    onClickUpdate: (Book) -> Unit,
    onClickDelete: (Book) -> Unit,
    navController: NavController,
    ) {
    // ストレージに保存してあるパスからuriを作成
    val uri: Uri? = convertStringToUri(book.imageUri)
    val context = LocalContext.current
    var imageBitmap: Bitmap? = null
    var showDiscribeDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // uriをBitmapに変換
    if (uri != null && uri.toString().isNotEmpty()) {
        imageBitmap = convertUriToBitmap(uri, context)
    }

    // 変換できなかった場合や画像が存在しない場合はデフォルトの画像を使用する
    if (imageBitmap == null) {
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.default_icon)
        imageBitmap = drawable?.toBitmap()
    }

    val archive = SwipeAction(
        icon = rememberVectorPainter(Icons.Default.Build),
        background = Color.Green,
        onSwipe = { }
    )

    val snooze = SwipeAction(
        icon = {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "削除する",
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        },
        background = Color.Red,
        isUndo = true,
        onSwipe = {
            showDeleteDialog = true
        },
    )

    SwipeableActionsBox(
        startActions = listOf(archive),
        endActions = listOf(snooze)
    ) {
        // Swipeable content goes here.
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(100.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Row(
                modifier = Modifier.combinedClickable(
                    onClick = {
                        navController.navigate("${NavigationItem.Home.route}/${book.id}")
                    },
                    onLongClick = {
                        showDiscribeDialog = true
                    },
                    onClickLabel = "図鑑説明表示用ロングタップ"
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = "Book Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = book.title,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            fontSize = 24.sp,
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onClickUpdate(book)
                    },
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Book edit")
                }
            }
            // 図鑑説明用ダイアログ表示
            if (showDiscribeDialog) {
                AlertDialog(
                    onDismissRequest = { showDiscribeDialog = false },
                    title = { Text(book.title) },
                    text = { Text(book.description) },
                    confirmButton = {

                    },
                    dismissButton = {
                        Button(onClick = { showDiscribeDialog = false }) {
                            Text("戻る")
                        }
                    }
                )
            }
            if (showDeleteDialog) {
                AlertDialog(
                    title = { Text(text ="本当に図鑑を削除しますか？")},
                    onDismissRequest = { showDeleteDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                            showDeleteDialog = false
                            onClickDelete(book)
                        }) {
                            Text("削除する")
                        }
                        // TODO 図鑑の画像も削除が必要
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteDialog = false }) {
                            Text("戻る")
                        }
                    }
                )
            }
        }

    }
}