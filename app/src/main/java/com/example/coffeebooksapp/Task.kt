package com.example.coffeebooksapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Books(
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

        other as Books

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
    tableName = "book_items",
    foreignKeys = [
        ForeignKey(
            entity = Books::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE // 必要に応じてonDeleteを指定
        )
    ]
)
data class BooksItem(
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

        other as BooksItem

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}