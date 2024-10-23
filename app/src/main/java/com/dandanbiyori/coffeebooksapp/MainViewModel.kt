package com.dandanbiyori.coffeebooksapp

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val bookDao: BookDao) : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var bookImageUri by mutableStateOf("")
    var type: BooksType by mutableStateOf(BooksType.USER_CREATED)

    var isShowDialog by mutableStateOf(false)
    var isUserBook by mutableStateOf(false)

    // distinctUntilChanged:値が同じ場合は無視する
    val books = bookDao.loadAllBooks().distinctUntilChanged()

    // 状態判別
    private var editingBook: Book? = null
    val isEditing: Boolean
        get() = editingBook != null

    fun setEditingBook(book: Book) {
        editingBook = book
        title = book.title
        description = book.description
        bookImageUri = book.imageUri
    }

    fun createBook() {
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
        viewModelScope.launch(Dispatchers.IO) {
            bookDao.deleteBook(book)
            Util.deleteImageInternalStorage(book.imageUri)
        }
    }

    fun updateBook() {
        editingBook?.let { book ->
            viewModelScope.launch {
                book.title = title
                book.description = description
                book.imageUri = if (bookImageUri == "") "" else bookImageUri
                bookDao.updateBook(book)
            }
        }
    }

    fun resetProperties() {
        editingBook = null
        title = ""
        description = ""
        bookImageUri = ""
    }
}

@HiltViewModel
class BookItemViewModel @Inject constructor(private val bookItemDao: BookItemDao) : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    val bookId by mutableStateOf("")
    var bookItemImageUri by mutableStateOf("")

    var isShowDialog by mutableStateOf(false)
    private var editingBookItem: BookItem? = null

    // 変更後
    private val _bookItem = MutableStateFlow<BookItem?>(null)
    val bookItem = _bookItem.asStateFlow()

    // distinctUntilChanged:値が同じ場合は無視する
    val bookItems = bookItemDao.loadAllBookItems().distinctUntilChanged()

    private val _bookItemsByBookId = MutableStateFlow<List<BookItem>>(emptyList())
    var bookItemsByBookId = _bookItemsByBookId.asStateFlow()

    val isEditing: Boolean
        get() = editingBookItem != null

    // Coffeeitem
    var roast by mutableStateOf("")
    var flavor by mutableStateOf("")
    var varieties by mutableStateOf("")
    var country by mutableStateOf("")
    var processing by mutableStateOf("")

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri
    private val _isUpdating = mutableStateOf(false)
    val isUpdating: State<Boolean> = _isUpdating

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun updateImage(uri: Uri?) {
        _imageUri.value = uri
        finishUpdating()
    }
    fun startUpdating() {
        _isUpdating.value = true
    }

    fun finishUpdating() {
        _isUpdating.value = false
    }


    fun createBookItem(bookId: Int) {
        viewModelScope.launch {
            val newBookItem = BookItem(
                title = title,
                bookId = bookId,
                description = description,
                imageUri = bookItemImageUri,
                type = BooksItemType.SIMPLE_ITEM,
                roast = roast,
                flavor = flavor,
                varieties = varieties,
                country = country,
                processing = processing
            )
            bookItemDao.insertBookItem(newBookItem)
        }
    }


    fun createCoffeeBookItem(bookId: Int) {
        Log.e("createCoffeeBookItem","CoffeeBookItemが作成されました。")
        viewModelScope.launch {
            val newBookItem = BookItem(
                title = title,
                bookId = bookId,
                description = description,
                imageUri = bookItemImageUri,
                type = BooksItemType.COFFEE_ITEM,
                roast = roast,
                flavor = flavor,
                varieties = varieties,
                country = country,
                processing = processing
            )
            bookItemDao.insertBookItem(newBookItem)
        }
    }

    fun setEditingBookItem(bookItem: BookItem) {
        editingBookItem = bookItem
        title = bookItem.title
        description = bookItem.description
        bookItemImageUri = bookItem.imageUri
        Log.e("setEditingBookItem","BookItemがセットされました。")
    }

    fun setEditingCoffeeBookItem(bookItem: BookItem) {
        Log.e("setEditingCoffeeBookItem","CoffeeBookItemがセットされました。")
        editingBookItem = bookItem
        title = bookItem.title
        description = bookItem.description
        bookItemImageUri = bookItem.imageUri
        roast = bookItem.roast
        flavor = bookItem.flavor
        varieties = bookItem.varieties
        country = bookItem.country
        processing = bookItem.processing
    }

    fun deleteBookItem(bookItem: BookItem) {
        viewModelScope.launch {
            bookItemDao.deleteBookItem(bookItem)
        }
    }

    fun updateBookItem() {
        editingBookItem?.let { bookItem ->
            viewModelScope.launch {
                bookItem.title = title
                bookItem.description = description
                bookItem.imageUri = if (bookItemImageUri == "") "" else bookItemImageUri
                bookItemDao.updateBookItem(bookItem)
            }
        }
    }

    fun updateCoffeeBookItem() {
        editingBookItem?.let { bookItem ->
            viewModelScope.launch {
                bookItem.title = title
                bookItem.country = country
                bookItem.flavor = flavor
                bookItem.processing = processing
                bookItem.roast = roast
                bookItem.varieties = varieties
                bookItemDao.updateBookItem(bookItem)
            }
        }
    }

    fun resetProperties() {
        editingBookItem = null
        title = ""
        description = ""
        bookItemImageUri = ""
        roast = ""
        flavor = ""
        varieties = ""
        country = ""
        processing = ""
        _imageUri.value = null
    }

    // BookItemをセットする
    // 変更後
    fun setBookItem(id: Int) {
        viewModelScope.launch {
            bookItemDao.getBookItem(id).collect {
                _bookItem.value = it
            }
        }
    }


    // BookItemをリセットする
    fun resetBookItem() {
        _bookItem.value = null
    }

    fun setBookItemsByBookId(bookId: Int){
        viewModelScope.launch {
            bookItemDao.getBookItems(bookId).collect {
                Log.e("getBookItemsByBookId","BookItemが取得されました。")
                Log.e("getBookItemsByBookId",it.toString())
                _bookItemsByBookId.value = it
            }
        }
    }
}