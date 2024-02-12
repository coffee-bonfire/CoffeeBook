package com.dandanbiyori.coffeebooksapp.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.Book
import com.dandanbiyori.coffeebooksapp.BookViewModel

@Composable
fun HomeScreen(books:List<Book>, bookViewModel: BookViewModel, navController: NavController) {
    BookList(
        books = books,
        onClickRow = {

        },
        onClickUpdate ={
            bookViewModel.setEditingBook(it)
            bookViewModel.isShowDialog = true
        },
        onClickDelete = { bookViewModel.deleteBook(it) },
        navController
    )
}