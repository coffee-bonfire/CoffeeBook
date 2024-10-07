package com.dandanbiyori.coffeebooksapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String,
    var imageUri: String,
    val type: BooksType
)

// ユーザが作成した図鑑かシステムが提供した図鑑かを示す
enum class BooksType {
    USER_CREATED,
    SYSTEM_PROVIDED
}

@Entity(
    tableName = "bookItems",
    indices = [Index("bookId")],
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onUpdate = ForeignKey.CASCADE, // 親テーブル更新時に子テーブルも同様に更新
            onDelete = ForeignKey.CASCADE // 親テーブル更新時に子テーブルも同様に更新
        )
    ]
)
data class BookItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int, //Booksテーブルへの外部キー
    var title: String,
    var description: String,
    var imageUri: String,
    val type: BooksItemType,
    var roast:String,
    var flavor:String,
    var varieties:String,
    var country:String,
    var processing:String,
)
enum class BooksItemType {
    SIMPLE_ITEM,
    COFFEE_ITEM
}