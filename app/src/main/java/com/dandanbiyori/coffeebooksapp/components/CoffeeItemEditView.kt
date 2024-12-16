package com.dandanbiyori.coffeebooksapp.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dandanbiyori.coffeebooksapp.BookItemViewModel
import com.dandanbiyori.coffeebooksapp.R
import com.dandanbiyori.coffeebooksapp.Util
import com.dandanbiyori.coffeebooksapp.Util.Companion.saveBookItemImageToInternalStorage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeItemEdit(
    onClickBack: () -> Unit,
    bookIdForDialog: Int,
    navController: NavController,
    bookItemViewModel: BookItemViewModel,
    context: Context
) {
    Log.e("test:title:","" + bookItemViewModel.title)
    Log.e("test:uri:","" + bookItemViewModel.bookItemImageUri)

    var showDialog by remember { mutableStateOf(false) }
    val maxTitleLength = 20
    val maxCountryLength = 20
    val maxVarietiesLength = 20
    val maxProcessingLength = 20
    val maxFlavorLength = 30
    val maxRoastLength = 15
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageFlag by remember { mutableStateOf(false) }

    // バックボタンが押されたときの処理
    BackHandler {
        showDialog = true
    }

    // Composable の終了時に viewModel の resetProperties を呼び出す
    DisposableEffect(Unit) {
        onDispose {
            bookItemViewModel.resetProperties()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (bookItemViewModel.isEditing) Text("Edit Item") else Text("Create Item")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD3A780)),
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Are you sure?") },
                text = { Text("Do you want to discard your changes?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            onClickBack()
                            bookItemViewModel.resetProperties()
                        },
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                    ) {
                        Text("Cancel")
                    }
                }

            )
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(scrollState)
            // Column の幅を最大化
        ) {
            LoadCoffeeItemImage(
                { imageUri ->
                    selectedImageUri = imageUri
                },
                bookItemViewModel.bookItemImageUri,
                bookItemViewModel
            )

            Card(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .imePadding(),
                ) {
                    // 各項目を OutlinedTextField でラップし、ラベルを追加
                    OutlinedTextField(
                        value = bookItemViewModel.title,
                        onValueChange = {
                            if (it.length <= maxTitleLength) {
                                bookItemViewModel.title = it
                            }
                        },
                        label = { Text("title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bookItemViewModel.country,
                        onValueChange = {
                            if (it.length <= maxCountryLength) {
                                bookItemViewModel.country = it
                            }
                        },
                        label = { Text("country") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bookItemViewModel.varieties,
                        onValueChange = {
                            if (it.length <= maxVarietiesLength) {
                                bookItemViewModel.varieties = it
                            }
                        },
                        label = { Text("varieties") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = bookItemViewModel.processing,
                        onValueChange = {
                            if (it.length <= maxProcessingLength) {
                                bookItemViewModel.processing = it
                            }
                        },
                        label = { Text("processing") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = bookItemViewModel.flavor,
                        onValueChange = {
                            if (it.length <= maxFlavorLength) {
                                bookItemViewModel.flavor = it
                            }
                        },
                        label = { Text("flavor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = bookItemViewModel.roast,
                        onValueChange = {
                            if (it.length <= maxRoastLength) {
                                bookItemViewModel.roast = it
                            }
                        },
                        label = { Text("roast") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Card と Button の間にスペースを追加

            Button(
                onClick = {
                    // 編集中の場合は更新、それ以外の場合は新規作成
                    if (bookItemViewModel.isEditing) {
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
                        bookItemViewModel.updateCoffeeBookItem()
                    } else {
                        // 画像を選択している場合は画像を保存
                        selectedImageUri?.let { uri ->
                            // 選択された画像を内部ストレージに保存し、URIをViewModelに設定
                            val savedImageUri =
                                saveBookItemImageToInternalStorage(uri, context)
                            bookItemViewModel.bookItemImageUri = savedImageUri
                        }
                        bookItemViewModel.createCoffeeBookItem(bookIdForDialog)
                    }
                    onClickBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .imePadding(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD3A780),
                    contentColor = Color.White
                )

            ) {
                Text(
                    text = "Save",
                    color = Color.Black
                )
            }
        }


    }
}

@Composable
fun LoadCoffeeItemImage(
    onImageLoaded: (Uri) -> Unit,
    bookImageUriString: String,
    bookItemViewModel: BookItemViewModel
) {
    // ViewModel の状態を使用
    val imageUri by bookItemViewModel.imageUri
    val isUpdating by bookItemViewModel.isUpdating
    val bookImageUri = if (bookImageUriString == "") "" else Util.convertStringToUri(
        bookImageUriString
    )

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            Log.e("ImagePicker", "Selected image URI: $uri")
            // imageUriに反映
            bookItemViewModel.updateImage(uri)
            // URIが変更されたときにコールバックを呼び出し
            uri?.let {
                onImageLoaded(it)
            }
        }
    val iconModifier = Modifier
        .size(Dimens.ToolbarIconSize)
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = CircleShape
        )
        .clip(CircleShape)

    // 画像が選択されていなければ空のBoxを表示
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // 画像の想定サイズ
            .background(Color.LightGray)
    ) {
        if (isUpdating) {
            // 更新中はプログレスバーを表示
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (imageUri != Uri.EMPTY && imageUri != null) {
            Log.e("ImagePicker", "Selected image URI: $imageUri")
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUri
                ),
                contentDescription = stringResource(R.string.dialog_user_select_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            // 選択した画像が存在する場合は画像更新用ボタンを表示
            // 画像更新ボタン
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = Dimens.ToolbarIconPadding)
                    .then(iconModifier)
                    .size(50.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape),
                onClick = {
                    bookItemViewModel.startUpdating()
                    launcher.launch("image/*")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "図鑑画像更新"
                )
            }
        } else {
            if (bookImageUri != null && bookImageUri != Uri.EMPTY) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = bookImageUri
                    ),
                    contentDescription = stringResource(R.string.dialog_user_select_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                // 画像が選択されていない場合は空のBoxを表示
                // bookImageUriString の画像を表示
                // bookImageUriをUriに変換
                IconButton(
                    onClick = {
                        bookItemViewModel.startUpdating()
                        launcher.launch("image/*")
                    },
                    modifier = Modifier
                        .padding(end = Dimens.ToolbarIconPadding)
                        .then(iconModifier)
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "図鑑画像追加"
                    )
                }
            } else {
                // 画像が選択されていない場合は空のBoxを表示
                // bookImageUriString の画像を表示
                // bookImageUriをUriに変換
                IconButton(
                    onClick = {
                        bookItemViewModel.startUpdating()
                        launcher.launch("image/*")
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "図鑑画像追加"
                    )
                }
            }
        }
    }
}