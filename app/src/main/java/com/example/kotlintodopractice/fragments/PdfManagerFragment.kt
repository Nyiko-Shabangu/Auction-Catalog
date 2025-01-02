package com.example.kotlintodopractice.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.databinding.FragmentPdfManagerBinding
import com.example.kotlintodopractice.utils.adapter.PdfListAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator

class PdfManagerFragment : Fragment() {

    private lateinit var binding: FragmentPdfManagerBinding
    private val pdfList = mutableListOf<Map<String, String>>()
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfManagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        // Setup RecyclerView
        binding.pdfRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.backHome.setOnClickListener {
            navController.navigate(R.id.action_pdfManagerFragment_to_homeFragment)
        }

        //action_pdfManagerFragment_to_homeFragment
        // Load PDFs from Firebase
        loadPdfs()
    }

    private fun loadPdfs() {
        FirebaseDatabase.getInstance().reference.child("pdfs")
            .get()
            .addOnSuccessListener { snapshot ->
                pdfList.clear()
                for (childSnapshot in snapshot.children) {
                    val typeIndicator = object : GenericTypeIndicator<Map<String, String>>() {}
                    val pdf = childSnapshot.getValue(typeIndicator)

                    if (pdf != null) {
                        pdfList.add(pdf)
                    }
                }
                binding.pdfRecyclerView.adapter = PdfListAdapter(pdfList,
                    onViewClick = { pdf ->
                        pdf["url"]?.let { openPdf(it) }
                    },
                    onDeleteClick = { pdf ->
                        pdf["name"]?.let { deletePdf(it) }
                    })
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load PDFs.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openPdf(pdfUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePdf(pdfName: String) {
        FirebaseDatabase.getInstance().reference.child("pdfs")
            .orderByChild("name").equalTo(pdfName).get()
            .addOnSuccessListener { snapshot ->
                for (childSnapshot in snapshot.children) {
                    childSnapshot.ref.removeValue()
                }
                Toast.makeText(context, "PDF deleted successfully.", Toast.LENGTH_SHORT).show()
                loadPdfs()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete PDF.", Toast.LENGTH_SHORT).show()
            }
    }
}
