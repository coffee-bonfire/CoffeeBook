package com.dandanbiyori.coffeebooksapp.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dandanbiyori.coffeebooksapp.Util.Companion.convertStringToUri
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

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    val deleteBookAction = SwipeAction(
        icon = {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Delete",
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
        endActions = listOf(deleteBookAction)
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

                AsyncImage(
                    model = if (uri != null && uri.toString().isNotEmpty()) {
                        uri
                    } else {
                        ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.default_icon)
                            .build()
                    },
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
                        // タイトルが長すぎる場合は省略する
                        text = if (book.title.length > 10) {
                            book.title.substring(0, 10) + "..."
                        } else {
                            book.title
                        },
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            fontSize = 24.sp,
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Column {
                    IconButton(
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier.size(24.dp),
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
                                onClickUpdate(book)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                expanded = false
                                selectedOption = "delete"
                                showDeleteDialog = true
                            }
                        )
                    }

                }
                // 図鑑説明用ダイアログ表示
                if (showDiscribeDialog) {
                    AlertDialog(
                        onDismissRequest = { showDiscribeDialog = false },
                        title = if (book.title.length > 10) {
                            { Text(book.title.substring(0, 10) + "...") }
                        } else {
                            { Text(book.title) }
                        },

                        text = if (book.description.length > 100) {
                            { Text(book.description.substring(0, 10) + "...") }
                        } else {
                            { Text(book.description) }
                        },
                        confirmButton = {
                            // 何もしない
                        },
                        dismissButton = {
                            Button(onClick = { showDiscribeDialog = false }) {
                                Text("Back")
                            }
                        }
                    )
                }
                if (showDeleteDialog) {
                    AlertDialog(
                        title = { Text(text = "Do you really want to delete the book?") },
                        onDismissRequest = { showDeleteDialog = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDeleteDialog = false
                                    onClickDelete(book)
                                }) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteDialog = false }) {
                                Text("Back")
                            }
                        }
                    )
                }
            }

        }
    }
}