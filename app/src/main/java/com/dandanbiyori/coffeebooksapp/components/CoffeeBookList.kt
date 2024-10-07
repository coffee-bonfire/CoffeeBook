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
    onClickUpdate: (Book) -> Unit,
    onClickDelete: (Book) -> Unit,
    navController: NavController
){
    // SystemBooks
    LazyColumn {
        items(
            books,
            // keyを指定することで、更新時にのみ更新される
            key = {
                book -> book.id
            }
        ) { book ->
            if( book.type == BooksType.SYSTEM_PROVIDED ){
                BookRow(
                    book = book,
                    onClickUpdate = onClickUpdate,
                    onClickDelete = onClickDelete,
                    navController
                )
            }
        }
    }

}