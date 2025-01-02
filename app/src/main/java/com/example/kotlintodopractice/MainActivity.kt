package com.example.kotlintodopractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        Log.d("FirebaseDebug", "FirebaseApp initialized successfully.")

        // Test Firebase connectivity
        FirebaseDatabase.getInstance().reference.child("testConnection").setValue("test")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseDebug", "Connected to Firebase")
                } else {
                    Log.e("FirebaseDebug", "Firebase connection failed: ${task.exception?.message}")
                }
            }

        // Reference to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")



        // Reading data from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("FirebaseDebug", "Data read successfully: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("FirebaseDebug", "Failed to read value: ${error.toException()}")
            }
        })


    }
}
