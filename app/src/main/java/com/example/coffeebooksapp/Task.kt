package com.example.coffeebooksapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String,
    var image: ByteArray,
    val type: BooksType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}

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
    val id: Int,
    val bookId: Int, //Booksテーブルへの外部キー
    var title: String,
    var description: String,
    var image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookItem

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}