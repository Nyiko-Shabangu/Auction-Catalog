package com.example.kotlintodopractice.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.databinding.FragmentHomeBinding
import com.example.kotlintodopractice.utils.FirebaseUtils
import com.example.kotlintodopractice.utils.ImageUtils
import com.example.kotlintodopractice.utils.LotManager
import com.example.kotlintodopractice.utils.adapter.TaskAdapter
import com.example.kotlintodopractice.utils.model.ToDoData
import com.example.kotlintodopractice.utils.pdf.PdfGenerator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.io.File

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<ToDoData>()
    private var selectedImageUri: Uri? = null
    private lateinit var navController: NavController


    private val taskList = mutableListOf<ToDoData>()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(
            tasks,
            onEditClick = { task ->
              //  showEditTaskDialog(task) // Define this function to handle task edits
            },
            onDeleteClick = { task ->
                deleteTask(task) // Define this function to handle task deletions
            }
        )
        binding.mainRecyclerView.adapter = taskAdapter

        binding.addTaskBtn.setOnClickListener {
            showAddTaskDialog()
        }

        binding.generatePdfBtn.setOnClickListener {
            val selectedLots = taskAdapter.getSelectedLots()
            if (selectedLots.isNotEmpty()) {
                lifecycleScope.launch {
                    PdfGenerator.generatePdf(requireContext(), selectedLots)
                }
            } else {
                Toast.makeText(context, "No lots selected!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.takePDFManager.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_pdfManagerFragment)
        }


        loadTasks()
    }




    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val lotIdEditText = dialogView.findViewById<EditText>(R.id.lotIdEditText)
        val lotNoEditText = dialogView.findViewById<EditText>(R.id.lotNoEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)
        val regNoEditText = dialogView.findViewById<EditText>(R.id.regNoEditText)
        val taskPhotoImageView = dialogView.findViewById<ImageView>(R.id.taskPhotoImageView)

        taskPhotoImageView.setOnClickListener {
            val pickImageIntent = Intent(Intent.ACTION_PICK)
            pickImageIntent.type = "image/*"
            startActivityForResult(pickImageIntent, 1001)
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Add New Task")
            .setPositiveButton("Add") { _, _ ->
                val lotId = lotIdEditText.text.toString()
                val lotNo = lotNoEditText.text.toString().toIntOrNull() ?: 0
                val description = descriptionEditText.text.toString()
                val regNo = regNoEditText.text.toString()

                if (lotId.isNotEmpty() && selectedImageUri != null) {
                    resizeAndUploadImage(lotId) { imageUrl ->
                        if (imageUrl != null) {
                            saveTask(lotId, lotNo, description, regNo, imageUrl)
                        } else {
                            Toast.makeText(context, "Image upload failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Lot ID and image are required.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

//    private fun showEditTaskDialog(task: ToDoData) {
//        // Implement the logic to display a dialog where the user can edit the task
//        // For example:
//        val dialog = EditTaskDialogFragment(task) // Create a custom dialog fragment for editing
//        dialog.setOnTaskEditedListener { updatedTask ->
//            updateTaskInDatabase(updatedTask) // Save changes to the database
//        }
//        dialog.show(parentFragmentManager, "EditTaskDialog")
//    }

    private fun deleteTask(task: ToDoData) {
        val taskRef = FirebaseDatabase.getInstance().reference.child("tasks")
        taskRef.orderByChild("lot_ID").equalTo(task.LOT_ID).get()
            .addOnSuccessListener { snapshot ->
                for (child in snapshot.children) {
                    child.ref.removeValue()
                        .addOnCompleteListener { taskResult ->
                            if (taskResult.isSuccessful) {
                                tasks.remove(task)
                                taskAdapter.notifyDataSetChanged()
                                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to locate task: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }



    private fun resizeAndUploadImage(lotId: String, onComplete: (String?) -> Unit) {
        try {
            if (selectedImageUri == null) {
                Log.e("TaskDialog", "No image selected.")
                onComplete(null)
                return
            }

            val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri!!)
            if (inputStream == null) {
                Log.e("TaskDialog", "Input stream is null. Failed to open image.")
                onComplete(null)
                return
            }

            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            if (originalBitmap == null) {
                Log.e("TaskDialog", "Failed to decode Bitmap from input stream.")
                onComplete(null)
                return
            }

            Log.d("TaskDialog", "Original Bitmap size: ${originalBitmap.width}x${originalBitmap.height}")

            // Resize the bitmap and write to a temporary file
            val tempFile = File.createTempFile("resized_image", ".jpg")
            val resizedBytes = ImageUtils.resizeImageBitmap(originalBitmap, 300, 300)
            if (resizedBytes.isEmpty()) {
                Log.e("TaskDialog", "Resized image byte array is empty.")
                onComplete(null)
                return
            }

            tempFile.writeBytes(resizedBytes)

            // Upload the resized image
            FirebaseUtils.uploadResizedImage(tempFile.absolutePath, lotId) { imageUrl ->
                tempFile.delete() // Clean up temporary file
                Log.d("TaskDialog", "Image uploaded successfully. URL: $imageUrl")
                onComplete(imageUrl)
            }
        } catch (e: Exception) {
            Log.e("TaskDialog", "Error resizing or uploading image: ${e.message}")
            onComplete(null)
        }
    }


    private fun saveTask(lotId: String, lotNo: Int, description: String, regNo: String, imageUrl: String) {
        val newTask = ToDoData(lotId, lotNo, description, regNo, imageUrl)
        FirebaseDatabase.getInstance().reference.child("tasks").push()
            .setValue(newTask)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Task added successfully!", Toast.LENGTH_SHORT).show()
                    loadTasks()
                } else {
                    Toast.makeText(context, "Failed to add task.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loadTasks() {
        val taskRef = FirebaseDatabase.getInstance().reference.child("tasks")
        taskRef.get().addOnSuccessListener { snapshot ->
            tasks.clear()
            for (taskSnapshot in snapshot.children) {
                val task = taskSnapshot.getValue(ToDoData::class.java)
                if (task != null) tasks.add(task)
            }
            taskAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load tasks.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editLot(lot: ToDoData) {
        val newDetails = ToDoData(
            LOT_ID = lot.LOT_ID,
            LOT_NO = 123, // Example new details
            DESCRIPTION = "Updated Description",
            REG_NO = "Updated Reg No",
            imageUrl = "Updated Image URL"
        )

        lifecycleScope.launch {
            LotManager.editLot(requireContext(), lot, newDetails)
            loadTasks() // Refresh the list after editing
        }
    }

    private fun deleteLot(lot: ToDoData) {
        lifecycleScope.launch {
            LotManager.deleteLot(requireContext(), lot)
            loadTasks() // Refresh the list after deletion
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                Log.d("TaskDialog", "Selected Image URI: $selectedImageUri")
            } else {
                Log.e("TaskDialog", "No image selected.")
            }
        }
    }
}
