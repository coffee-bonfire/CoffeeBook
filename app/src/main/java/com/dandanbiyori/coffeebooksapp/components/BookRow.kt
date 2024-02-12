package com.dandanbiyori.coffeebooksapp.components

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
import com.dandanbiyori.coffeebooksapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRow(
    book: (Book),
    onCllickRow:(Book) -> Unit,
    onClickUpdate:(Book)-> Unit,
    onClickDelete: (Book) -> Unit,
    navController: NavController
){
    val uri:Uri? = convertStringToUri(book.imageUri)
    val context = LocalContext.current
    var imageBitmap: Bitmap? = null
    if (uri != null){
        imageBitmap = convertUriToBitmap(uri,context)
    }

    if (imageBitmap == null){
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.default_icon)
        imageBitmap = drawable?.toBitmap()
    }

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
            modifier = Modifier
                .clickable {
//                    onCllickRow(book)
                    // TODO ROWをクリックすると画面遷移するようにする
                    navController.navigate("${NavigationItem.Home.route}/${book.id}")
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp).fillMaxHeight()
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
//                    val file = File(uri.path ?: "")
//                    if (file.exists()) {
//                        file.delete()
//                    }
                },
                modifier = Modifier.size(24.dp),
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Book edit")
            }
        }
    }
}

fun bitmapResize(bitmap:Bitmap):Bitmap{
    val matrix = Matrix()
    matrix.postScale(0.5f, 0.5f) // 0.5倍調整
    val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    return scaledBitmap
}