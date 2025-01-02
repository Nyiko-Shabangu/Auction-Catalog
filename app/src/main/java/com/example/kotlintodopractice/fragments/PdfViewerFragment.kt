package com.example.kotlintodopractice.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.databinding.FragmentPdfViewerBinding
import com.example.kotlintodopractice.utils.adapter.PdfListAdapter
import com.example.kotlintodopractice.utils.adapter.PdfViewerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class PdfViewerFragment : Fragment() {

    private lateinit var binding: FragmentPdfViewerBinding
    private val pdfList = mutableListOf<Map<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.pdfRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Load PDFs from Firebase
        loadPdfs()
    }

    private fun loadPdfs() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("pdfs")
        databaseReference.get().addOnSuccessListener { snapshot ->
            pdfList.clear()
            val typeIndicator = object : GenericTypeIndicator<Map<String, String>>() {}
            for (child in snapshot.children) {
                val pdfData = child.getValue(typeIndicator)
                if (pdfData != null) {
                    pdfList.add(pdfData)
                }
            }
            if (pdfList.isEmpty()) {
                Toast.makeText(context, "No PDFs found.", Toast.LENGTH_SHORT).show()
            } else {
                binding.pdfRecyclerView.adapter = PdfListAdapter(pdfList, onViewClick = { pdf ->
                    pdf["url"]?.let { openPdf(it) }
                }) { pdf ->
                    pdf["url"]?.let { openPdf(it) }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch PDFs: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openPdf(pdfUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to open PDF.", Toast.LENGTH_SHORT).show()
        }
    }
}
