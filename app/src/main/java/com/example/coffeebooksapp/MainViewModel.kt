package com.example.coffeebooksapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val bookDao: BookDao):ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var bookImageUri by mutableStateOf("")
    var type:BooksType by mutableStateOf(BooksType.USER_CREATED)

    var isShowDialog by mutableStateOf(false)

    // distinctUntilChanged:値が同じ場合は無視する
    val books = bookDao.loadAllBooks().distinctUntilChanged()

    // 状態判別
    private var editingBook: Book? = null
    val isEditing: Boolean
        get() = editingBook != null

    fun setEditingBook(book:Book){
        editingBook = book
        title = book.title
        description = book.description
        bookImageUri = book.imageUri
    }

    fun createBook(){
        viewModelScope.launch {
            val newBook = Book(
                title = title,
                description = description,
                imageUri = bookImageUri,
                type = type
            )
            bookDao.insertBook(newBook)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookDao.deleteBook(book)
        }
    }

    fun updateBook(){
        editingBook?.let{book ->
            viewModelScope.launch{
                book.title = title
                book.description = description
                book.imageUri = if (bookImageUri == "") "" else bookImageUri
                bookDao.updateBook(book)
            }
        }
    }
}

@HiltViewModel
class BookItemViewModel @Inject constructor(private val bookItemDao: BookItemDao):ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    val bookId by mutableStateOf("")
    var bookItemImageUri by mutableStateOf("")
    fun createBookItem(){
        val newBook = BookItem(
            title = title,
            description = description,
            bookId = 1,
            imageUri = bookItemImageUri,
        )
    }
}