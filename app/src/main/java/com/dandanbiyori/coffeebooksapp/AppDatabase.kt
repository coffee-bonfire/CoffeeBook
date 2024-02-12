package com.dandanbiyori.coffeebooksapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class, BookItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookItemDao(): BookItemDao
}