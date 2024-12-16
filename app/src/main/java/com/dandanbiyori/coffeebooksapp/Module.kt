package com.dandanbiyori.coffeebooksapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {

    // fallbackToDestructiveMigration
    // https://developer.android.com/training/data-storage/room/migrating-db-versions
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,AppDatabase::class.java, "book_database")
        .fallbackToDestructiveMigration()
        .createFromAsset("predbdata/book_database.db")
        .build()

    @Provides
    fun provideBookDao(db: AppDatabase) = db.bookDao()

    @Provides
    fun provideBookItemDao(db: AppDatabase) = db.bookItemDao()
}