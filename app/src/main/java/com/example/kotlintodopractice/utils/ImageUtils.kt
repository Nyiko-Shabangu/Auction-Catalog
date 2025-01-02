package com.example.kotlintodopractice.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun resizeImageBitmap(originalBitmap: Bitmap, maxWidth: Int, maxHeight: Int): ByteArray {
        return try {
            // Calculate the scaling factor
            val aspectRatio: Float = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
            val newWidth: Int
            val newHeight: Int

            if (aspectRatio > 1) {
                // Landscape image
                newWidth = maxWidth
                newHeight = (newWidth / aspectRatio).toInt()
            } else {
                // Portrait image
                newHeight = maxHeight
                newWidth = (newHeight * aspectRatio).toInt()
            }

            // Resize the bitmap
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

            // Convert the resized bitmap to a byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            Log.e("ImageUtils", "Error resizing image: ${e.message}")
            ByteArray(0) // Return empty array on failure
        }
    }
}
