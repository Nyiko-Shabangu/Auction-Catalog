package com.example.kotlintodopractice.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.example.kotlintodopractice.utils.model.ToDoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LotManager {

    suspend fun editLot(context: Context, lot: ToDoData, newDetails: ToDoData) {
        try {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("lots").child(lot.LOT_ID)

            // Update lot details in Firebase
            withContext(Dispatchers.IO) {
                databaseReference.setValue(newDetails)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Lot updated successfully!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to update lot: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun deleteLot(context: Context, lot: ToDoData) {
        try {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("lots").child(lot.LOT_ID)

            // Delete lot from Firebase
            withContext(Dispatchers.IO) {
                databaseReference.removeValue()
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Lot deleted successfully!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to delete lot: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
