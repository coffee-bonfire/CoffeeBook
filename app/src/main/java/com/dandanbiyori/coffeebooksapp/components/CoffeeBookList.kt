package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.Book
import com.dandanbiyori.coffeebooksapp.BooksType

@Composable
fun CoffeeBookList(
    books: List<Book>,
    onClickRow: (Book) -> Unit,
    onClickUpdate: (Book) -> Unit,
    onClickDelete: (Book) -> Unit,
    navController: NavController
){
    // SystemBooks
    // TODO スクロール可能にする
    LazyColumn {
        items(books) { book ->
            if( book.type == BooksType.SYSTEM_PROVIDED ){
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