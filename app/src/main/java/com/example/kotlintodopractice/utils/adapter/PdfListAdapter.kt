package com.example.kotlintodopractice.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodopractice.R

class PdfListAdapter(
    private val pdfList: List<Map<String, String>>,
    private val onViewClick: (Map<String, String>) -> Unit,
    private val onDeleteClick: (Map<String, String>) -> Unit
) : RecyclerView.Adapter<PdfListAdapter.PdfViewHolder>() {

    class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfName: TextView = itemView.findViewById(R.id.pdfName)
        val viewButton: ImageButton = itemView.findViewById(R.id.viewButton) // Correct type
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton) // Correct type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdf = pdfList[position]
        val name = pdf["name"] ?: "Unknown PDF"
        holder.pdfName.text = name

        holder.viewButton.setOnClickListener { onViewClick(pdf) }
        holder.deleteButton.setOnClickListener { onDeleteClick(pdf) }
    }

    override fun getItemCount() = pdfList.size
}


