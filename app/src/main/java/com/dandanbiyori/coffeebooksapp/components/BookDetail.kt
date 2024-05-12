package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dandanbiyori.coffeebooksapp.BookItem
import com.dandanbiyori.coffeebooksapp.R

// Book詳細画面（BookItem一覧）
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetail(
    bookId: Int,
    onClickBack: () -> Unit,
    onClickUpdate: () -> Unit,
    bookItems : List<BookItem>
){
    val targetBookItems = mutableListOf<BookItem>()

    bookItems.forEach{ bookItem ->
        if( bookItem.bookId == bookId){
            targetBookItems.add(bookItem)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onClickUpdate()
                })
            {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "図鑑アイテム新規作成"
                )
            }
        }

    ) { padding ->
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.card_side_margin))
            ){
                items(
                    count = targetBookItems.size,
                    key = {index ->  index},
                    itemContent = { index ->
                        BookDeteilScreen(
                            padding,
                            targetBookItems[index]
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDeteilScreen (
    padding:PaddingValues,
    BookItem: BookItem
){
    Card(
        onClick = {
//            TODO
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
    ) {
        Column(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.default_icon),
                contentDescription = "bookdetail ",
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.book_item_image_height)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "テスト",
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.margin_normal))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }

}