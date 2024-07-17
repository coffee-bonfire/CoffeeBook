package com.dandanbiyori.coffeebooksapp.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.dandanbiyori.coffeebooksapp.BookItemViewModel
import com.dandanbiyori.coffeebooksapp.R
import com.dandanbiyori.coffeebooksapp.components.Util.Companion.saveBookItemImageToInternalStorage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItemEditDialog(
    context: Context,
    bookIdForDialog:Int,
    bookItemViewModel: BookItemViewModel = hiltViewModel(),
) {
    // EditDialogが非表示になるタイミングで実行される
    // 保持しているviewModelの値をクリアする
    DisposableEffect(Unit){
        onDispose {
            bookItemViewModel.resetProperties()
        }
    }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    AlertDialog(
        // ダイアログ外クリックでダイアログを閉じる
        onDismissRequest = {
            bookItemViewModel.isShowDialog = false
        },
        title = { Text(text = if (bookItemViewModel.isEditing) "図鑑アイテム更新" else stringResource(R.string.dialog_item_heading)) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(5.dp)
            ) {
                LoadItemImage(
                    { imageUri ->
                        selectedImageUri = imageUri
                    },
                    bookItemViewModel.bookItemImageUri
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.dialog_title))
                TextField(
                    value = bookItemViewModel.title,
                    onValueChange = { bookItemViewModel.title = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.dialog_description))
                TextField(
                    value = bookItemViewModel.description,
                    onValueChange = { bookItemViewModel.description = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        bookItemViewModel.isShowDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.dialog_cancel))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        bookItemViewModel.isShowDialog = false
                        if (bookItemViewModel.isEditing) {
                            // 画像を変更した場合には変更前の画像を削除する
                            selectedImageUri?.let { newUri ->
                                val oldBookUri: Uri? =
                                    Util.convertStringToUri(bookItemViewModel.bookItemImageUri)
                                val file = File(oldBookUri?.path ?: "")
                                if (file.exists()) {
                                    file.delete()
                                }
                                val savedImageUri = saveBookItemImageToInternalStorage(newUri, context)
                                bookItemViewModel.bookItemImageUri = savedImageUri
                            }
                            bookItemViewModel.updateBook()

                        } else {
                            // 画像を選択している場合は画像を保存
                            selectedImageUri?.let { uri ->
                                // 選択された画像を内部ストレージに保存し、URIをViewModelに設定
                                val savedImageUri = saveBookItemImageToInternalStorage(uri, context)
                                bookItemViewModel.bookItemImageUri = savedImageUri
                            }
                            bookItemViewModel.createBookItem(bookIdForDialog)
                        }
                    }
                ) {
                    Text(text = "OK")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
}

@Composable
fun LoadItemImage(onImageLoaded: (Uri) -> Unit, bookImageUriString: String) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            // URIが変更されたときにコールバックを呼び出し
            uri?.let {
                onImageLoaded(it)
            }
        }
    val bookImageUri = if (bookImageUriString == "") "" else Util.convertStringToUri(
        bookImageUriString
    )

    Column {
        Row {
            Button(
                onClick = {
                    launcher.launch("image/*")
                }
            ) {
                Icon(
                    imageVector = if (selectedImageUri == null && bookImageUri == "") Icons.Default.Add else Icons.Default.Refresh,
                    contentDescription = "図鑑画像追加・更新"
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            if (selectedImageUri != null) {
                Button(
                    onClick = {
                        selectedImageUri = null
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "図鑑画像削除"
                    )
                }
            }
        }

        if (selectedImageUri == null) {
            if (bookImageUri != ""){
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = rememberAsyncImagePainter(bookImageUri),
                    contentDescription = stringResource(R.string.dialog_user_select_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                )
            }
        } else {
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = stringResource(R.string.dialog_user_select_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            )
        }
    }
}