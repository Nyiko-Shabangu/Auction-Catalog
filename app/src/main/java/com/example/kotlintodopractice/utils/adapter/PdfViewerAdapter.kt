package com.example.kotlintodopractice.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodopractice.R

class PdfViewerAdapter(
    private val pdfList: List<Map<String, String>>,
    private val onViewClick: (String) -> Unit
) : RecyclerView.Adapter<PdfViewerAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfName: TextView = itemView.findViewById(R.id.pdfName)





        fun bind(pdf: Map<String, String>) {
            pdfName.text = pdf["name"] ?: "Unnamed PDF"
            itemView.setOnClickListener {
                val pdfUrl = pdf["url"]
                if (pdfUrl != null) {
                    onViewClick(pdfUrl)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.bind(pdfList[position])
    }

    override fun getItemCount(): Int = pdfList.size
}
