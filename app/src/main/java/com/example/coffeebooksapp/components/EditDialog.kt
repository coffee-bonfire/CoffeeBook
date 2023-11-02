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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.coffeebooksapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(isShowDialog : MutableState<Boolean>){
    AlertDialog(
        onDismissRequest = { isShowDialog.value = false },
        title = { Text(text = "図鑑新規作成") },
        text = {
               Column {
                   LoadImage()
                   Text(text = "タイトル")
                   TextField(value = "", onValueChange = {})
                   Text(text = "詳細")
                   TextField(value = "", onValueChange = {})
               }
        },
        confirmButton = { 
            Row (
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
            ){
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
                    }
                ) {
                    Text(text = "OK")
                }
            }
        }
    )
}

@Composable
fun UserImage(photoBitmap: ImageBitmap) {
    Image(
        painter = BitmapPainter(photoBitmap),
        contentDescription = null,  // Content description can be set based on your use case
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun LoadImage() {
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
        )
        {
            Text(text = "Load Image")
        }
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "My Image",
            modifier = Modifier.size(120.dp).clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}