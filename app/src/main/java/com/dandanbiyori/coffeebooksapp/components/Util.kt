package com.dandanbiyori.coffeebooksapp.components

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri

class Util {
    companion object {
        fun convertStringToUri(uriString:String): Uri? {
            val uri:Uri?
            try {
                uri = Uri.parse(uriString)
            } catch (e:Exception){
                e.printStackTrace()
                return null
            }
            return uri
        }
        fun convertUriToBitmap(uri:Uri,context: Context):Bitmap? {
            var imageBitmap: Bitmap? = null
            val contentResolver: ContentResolver = context.contentResolver

            try {
                // UriからInputStreamを取得
                val inputStream = contentResolver.openInputStream(uri)

                // InputStreamからBitmapを生成
                imageBitmap = BitmapFactory.decodeStream(inputStream)
                imageBitmap = bitmapResize(imageBitmap)

                // 必要に応じてInputStreamをクローズ
                inputStream?.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return imageBitmap
        }

        private fun bitmapResize(bitmap: Bitmap): Bitmap {
            val matrix = Matrix()
            matrix.postScale(0.5f, 0.5f) // 0.5倍調整
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }

}