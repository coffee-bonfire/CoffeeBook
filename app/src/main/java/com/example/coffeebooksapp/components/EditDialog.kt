package com.example.coffeebooksapp.components

import android.content.Context
import android.net.Uri
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
import com.example.coffeebooksapp.BookViewModel
import com.example.coffeebooksapp.R
import com.example.coffeebooksapp.components.Util.Companion.convertStringToUri
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    context: Context,
    bookViewModel: BookViewModel = hiltViewModel(),
) {
    // EditDialogが非表示になるタイミングで実行される
    // 保持しているviewModelの値をクリアする
    DisposableEffect(Unit){
        onDispose {
            bookViewModel.resetProperties()
        }
    }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    AlertDialog(
        onDismissRequest = { bookViewModel.isShowDialog = false },
        title = { Text(text = if (bookViewModel.isEditing) "図鑑更新" else stringResource(R.string.dialog_heading)) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(5.dp)
            ) {
                LoadImage(
                    { imageUri ->
                        selectedImageUri = imageUri
                    },
                    bookViewModel.bookImageUri
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.dialog_title))
                TextField(
                    value = bookViewModel.title,
                    onValueChange = { bookViewModel.title = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.dialog_description))
                TextField(
                    value = bookViewModel.description,
                    onValueChange = { bookViewModel.description = it }
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
                        bookViewModel.isShowDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.dialog_cancel))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        bookViewModel.isShowDialog = false
                        if (bookViewModel.isEditing) {
                            // 画像を変更した場合には変更前の画像を削除する
                            selectedImageUri?.let { newUri ->
                                val oldBookUri: Uri? =
                                    convertStringToUri(bookViewModel.bookImageUri)
                                val file = File(oldBookUri?.path ?: "")
                                if (file.exists()) {
                                    file.delete()
                                }
                                val savedImageUri = saveImageToInternalStorage(newUri, context)
                                bookViewModel.bookImageUri = savedImageUri
                            }
                            bookViewModel.updateBook()

                        } else {
                            // 画像を選択している場合は画像を保存
                            selectedImageUri?.let { uri ->
                                // 選択された画像を内部ストレージに保存し、URIをViewModelに設定
                                val savedImageUri = saveImageToInternalStorage(uri, context)
                                bookViewModel.bookImageUri = savedImageUri
                            }
                            bookViewModel.createBook()
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
fun LoadImage(onImageLoaded: (Uri) -> Unit, bookImageUriString: String) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            // URIが変更されたときにコールバックを呼び出し
            uri?.let {
                onImageLoaded(it)
            }
        }
    val bookImageUri = if (bookImageUriString == "") "" else convertStringToUri(bookImageUriString)

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

fun saveImageToInternalStorage(uri: Uri, context: Context): String {
    val imageFileName = "image_${System.currentTimeMillis()}.jpg"
    val internalStorageDir = context.filesDir
    val imageFile = File(internalStorageDir, imageFileName)

    // 画像を内部ストレージに保存
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(imageFile).use { output ->
            input.copyTo(output)
        }
    }
    // 保存された画像のURIを文字列として返す
    return Uri.fromFile(imageFile).toString()
}