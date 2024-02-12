package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.Book

@Composable
fun BookList(
    books: List<Book>,
    onClickRow: (Book) -> Unit,
    onClickUpdate:(Book) -> Unit,
    onClickDelete: (Book) -> Unit,
    navController: NavController
){
    LazyColumn()
    {
        items(books){
            book ->
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