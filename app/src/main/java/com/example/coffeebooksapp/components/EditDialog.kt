package com.example.coffeebooksapp.components

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.coffeebooksapp.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    isShowDialog: MutableState<Boolean>,
    bookViewModel: BookViewModel = hiltViewModel(),
) {
    var selectedImage: ByteArray? by remember { mutableStateOf(null) }

    AlertDialog(
        onDismissRequest = { isShowDialog.value = false },
        title = { Text(text = "図鑑新規作成") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(5.dp)
            ) {
                LoadImage { imageByteArray ->
                    selectedImage = imageByteArray
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "タイトル")
                TextField(
                    value = bookViewModel.title,
                    onValueChange = { bookViewModel.title = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "詳細")
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
                        isShowDialog.value = false
                    }
                ) {
                    Text(text = "キャンセル")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        isShowDialog.value = false
                        // 選択された画像をViewModelに設定
                        bookViewModel.bookImage = selectedImage
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
fun LoadImage(onImageLoaded: (ByteArray) -> Unit) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            // URIが変更されたときにコールバックを呼び出し
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.use { stream ->
                    val byteArray = stream.readBytes()
                    onImageLoaded(byteArray)
                }
            }
        }

    Column {
        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Icon(
                imageVector = if (imageUri == null) Icons.Default.Add else Icons.Default.Refresh,
                contentDescription = "図鑑画像"
            )
        }
        imageUri?.let {
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "My Image",
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