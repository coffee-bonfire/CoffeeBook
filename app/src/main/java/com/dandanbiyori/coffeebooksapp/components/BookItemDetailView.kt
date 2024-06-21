package com.dandanbiyori.coffeebooksapp.components


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import com.dandanbiyori.coffeebooksapp.BookItem
import com.dandanbiyori.coffeebooksapp.R

@Composable
fun BookItemDetailView(
    bookItemId: Int,
    onClickBack: () -> Unit,
    onClickUpdate: () -> Unit,
    bookItem: BookItem?
) {
    val scrollState = rememberScrollState()
    if (bookItem != null) {
        BookItemDetailContent(
            bookItem,
            scrollState
        )
    }

}

@Composable
fun BookItemDetailContent(
    bookItem: BookItem?,
    scrollState: ScrollState,
) {
    Column(Modifier.verticalScroll(scrollState)) {
        ConstraintLayout {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // image
                BookItemImage(bookItem)
                // title
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = bookItem!!.title,
                    color = Color.Gray,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp),
                ) {
                    Text(
                        text = bookItem.description,
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
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
    if (uri != null) {
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