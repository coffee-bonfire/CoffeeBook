package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.Book
import com.dandanbiyori.coffeebooksapp.BooksType

@Composable
fun UserBookList(
    books: List<Book>,
    onClickRow: (Book) -> Unit,
    onClickUpdate: (Book) -> Unit,
    onClickDelete: (Book) -> Unit,
    navController: NavController
) {

    // SystemBooks
    // TODO スクロール可能にする
    LazyColumn(
        modifier = Modifier.padding(10.dp)
    ) {
        items(books) { book ->
            if(book.type == BooksType.USER_CREATED) {
                BookRow(
                    book = book,
                    onCllickRow = onClickRow,
                    onClickUpdate = onClickUpdate,
                    onClickDelete = onClickDelete,
                    navController
                )
            }
        }
    }

}