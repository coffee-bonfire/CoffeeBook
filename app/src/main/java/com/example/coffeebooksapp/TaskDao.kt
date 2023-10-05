package com.example.coffeebooksapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM books")
    fun loadAllBooks(): Flow<List<Book>>

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}

@Dao
interface BookItemDao {
    @Insert
    suspend fun insertBookItem(book: BookItem)

    @Query("SELECT * FROM bookItems")
    fun loadAllBookItems(): Flow<List<BookItem>>

    @Update
    suspend fun updateBookItem(book: BookItem)

    @Delete
    suspend fun deleteBookItem(book: BookItem)
}