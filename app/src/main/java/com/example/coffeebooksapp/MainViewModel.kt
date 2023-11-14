package com.example.coffeebooksapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val bookDao: BookDao):ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var bookImage:ByteArray? by mutableStateOf(null)
    var type:BooksType by mutableStateOf(BooksType.USER_CREATED)

    fun createBook(){
        val newBook = Book(
            title = title,
            description = description,
            image = bookImage?: byteArrayOf(),
            type = type
        )
    }
}

@HiltViewModel
class BookItemViewModel @Inject constructor(private val bookItemDao: BookItemDao):ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    val bookId by mutableStateOf("")
    var bookItemImage:ByteArray? by mutableStateOf(null)
    fun createBookItem(){
        val newBook = BookItem(
            title = title,
            description = description,
            bookId = 1,
            image = bookItemImage?: byteArrayOf(),
        )
    }
}