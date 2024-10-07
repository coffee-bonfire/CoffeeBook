package com.dandanbiyori.coffeebooksapp.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.BookItemViewModel
import com.dandanbiyori.coffeebooksapp.NavigationItem
import com.dandanbiyori.coffeebooksapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeItemEdit(
    onClickBack: () -> Unit,
    bookIdForDialog:Int,
    navController: NavController,
    bookItemViewModel: BookItemViewModel,
) {
    var showDialog by remember { mutableStateOf(false) }
    val maxTitleLength = 20
    val maxCountryLength = 20
    val maxVarietiesLength = 20
    val maxProcessingLength = 20
    val maxFlavorLength = 30
    val maxRoastLength = 15

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
    Log.e("bookIdForDialog：", "：$bookIdForDialog")
    Log.e("title：", "：${bookItemViewModel.title}")
    Log.e("country：", "：${bookItemViewModel.country}")
    Log.e("varieties：", "：${bookItemViewModel.varieties}")
    Log.e("processing：", "：${bookItemViewModel.processing}")
    Log.e("flavor：", "：${bookItemViewModel.flavor}")
    Log.e("roast：", "：${bookItemViewModel.roast}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if(bookItemViewModel.isEditing) Text("Edit Item") else Text("Create Item")
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
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Are you sure?") },
                text = { Text("Do you want to discard your changes?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            navController.navigate("${NavigationItem.Home.route}/${bookIdForDialog}")
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
                .fillMaxWidth() // Column の幅を最大化
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_action_swipe_coffee_bean),
                contentDescription = "none",
                Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop,
            )
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(0.dp),
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
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
                        bookItemViewModel.updateCoffeeBookItem()
                    } else {
                        bookItemViewModel.createCoffeeBookItem(bookIdForDialog)

                    }
                    navController.navigate("${NavigationItem.Home.route}/${bookIdForDialog}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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