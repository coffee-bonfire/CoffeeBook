package com.dandanbiyori.coffeebooksapp

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class Util {
    companion object {
        fun convertStringToUri(uriString: String): Uri? {
            val uri: Uri?
            try {
                uri = Uri.parse(uriString)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return uri
        }

        fun saveBookImageToInternalStorage(uri: Uri, context: Context): String {
            val imageFileName = "image_${System.currentTimeMillis()}.jpg"
            val booksDir = File(context.filesDir, "Books")
            if (!booksDir.exists()) {
                booksDir.mkdirs()
            }

            val imageFile = File(booksDir, imageFileName)

            // 画像を内部ストレージに保存
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(imageFile).use { output ->
                    input.copyTo(output)
                }
            }
            // 保存された画像のURIを文字列として返す
            return Uri.fromFile(imageFile).toString()
        }

        fun saveBookItemImageToInternalStorage(
            uri: Uri,
            context: Context
        ): String {
            val imageFileName = "image_${System.currentTimeMillis()}.jpg"
            val bookItemsDir = File(context.filesDir, "BookItems")
            if (!bookItemsDir.exists()) {
                bookItemsDir.mkdirs()
            }

            val imageFile = File(bookItemsDir, imageFileName)

            // 画像を内部ストレージに保存
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(imageFile).use { output ->
                    input.copyTo(output)
                }
            }
            // 保存された画像のURIを文字列として返す
            return Uri.fromFile(imageFile).toString()
        }

        /**
         * 画像のパスを受け取り、内部ストレージから画像を削除する
         *
         * @param imageString 削除する画像のパス
         */
        fun deleteImageInternalStorage(imageString: String) {
            if (imageString.isEmpty()) {
                return
            }
            val context = App.instance.applicationContext
            val filesDir = context.filesDir.path
            val bookImageDir = "$filesDir/Books"
            val bookItemImageDir = "$filesDir/BookItems"

            try {
                val isBookImage = imageString.contains("files/Books")
                val isBookItemImage = imageString.contains("files/BookItems")

                val imageFileName = imageString.substring(imageString.lastIndexOf("/") + 1)

                if (isBookImage) {
                    deleteImage(bookImageDir, imageFileName)
                }

                if (isBookItemImage) {
                    deleteImage(bookItemImageDir, imageFileName)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 画像を削除するメソッド
        private fun deleteImage(imageDir: String, imageFileName: String) {
            val imageFile = File(imageDir, imageFileName)
            if (imageFile.exists()) {
                Log.d("deleteImageInternalStorage", "delete")
                imageFile.delete()
            } else {
                Log.e("deleteImageInternalStorage", "parentDirName: $imageFileName does not exist")
            }
        }

    }

}