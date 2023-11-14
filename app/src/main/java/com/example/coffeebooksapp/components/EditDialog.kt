package com.example.coffeebooksapp.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
            ) {
                LoadImage(bookViewModel.bookImage)
                Text(text = "タイトル")
                TextField(
                    value = bookViewModel.title,
                    onValueChange = { bookViewModel.title = it }
                )
                Text(text = "詳細")
                TextField(
                    value = bookViewModel.description,
                    onValueChange = { bookViewModel.description = it }
                )
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
            }
        }
    )
}

@Composable
fun LoadImage(image:ByteArray?) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column {
        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "図鑑画像")
        }
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "My Image",
            modifier = Modifier.size(120.dp).clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        )
    }
}