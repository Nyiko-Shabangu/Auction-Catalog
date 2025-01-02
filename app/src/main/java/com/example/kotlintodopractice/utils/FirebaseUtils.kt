package com.example.kotlintodopractice.utils

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

object FirebaseUtils {

    fun uploadResizedImage(imagePath: String, lotId: String, onComplete: (String?) -> Unit) {
        try {
            val storageReference = FirebaseStorage.getInstance().reference
                .child("lot_images/$lotId.jpg")

            val resizedImageBytes = File(imagePath).readBytes()
            if (resizedImageBytes.isEmpty()) {
                Log.e("FirebaseUtils", "Resized image file is empty. Aborting upload.")
                onComplete(null)
                return
            }

            storageReference.putBytes(resizedImageBytes)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        Log.d("FirebaseUtils", "Image uploaded successfully: $uri")
                        onComplete(uri.toString())
                    }.addOnFailureListener { e ->
                        Log.e("FirebaseUtils", "Failed to get download URL: ${e.message}")
                        onComplete(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseUtils", "Image upload failed: ${e.message}")
                    onComplete(null)
                }
        } catch (e: Exception) {
            Log.e("FirebaseUtils", "Error during upload: ${e.message}")
            onComplete(null)
        }
    }


    fun uploadPdf(pdfUri: Uri, fileName: String, callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("pdfs/$fileName")
        storageRef.putFile(pdfUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }.addOnFailureListener {
                    Log.e("FirebaseUtils", "Failed to get download URL: ${it.message}")
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseUtils", "Failed to upload file: ${it.message}")
                callback(null)
            }
    }

}
