package com.example.kotlintodopractice.utils.pdf

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.example.kotlintodopractice.utils.model.ToDoData
import com.example.kotlintodopractice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object PdfGenerator {

    suspend fun generatePdf(context: Context, selectedLots: List<ToDoData>) {
        try {
            // File path for saving PDF
            val pdfPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Vehicle_Auction_List.pdf")
            val pdfWriter = PdfWriter(pdfPath)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            // Add title
            document.add(Paragraph("VEHICLES AUCTION LIST").setFontSize(18f))

            // Create table with columns
            val table = Table(floatArrayOf(1f, 3f, 2f, 3f))
            table.addCell(Cell().add(Paragraph("LOT NO").setBold()))
            table.addCell(Cell().add(Paragraph("DESCRIPTION").setBold()))
            table.addCell(Cell().add(Paragraph("REG. NO").setBold()))
            table.addCell(Cell().add(Paragraph("PICTURES").setBold()))

            // Add rows to the table
            for (lot in selectedLots) {
                table.addCell(Cell().add(Paragraph(lot.LOT_NO.toString())))
                table.addCell(Cell().add(Paragraph(lot.DESCRIPTION)))
                table.addCell(Cell().add(Paragraph(lot.REG_NO)))

                // Load and add image to the table
                val imageCell = Cell()
                try {
                    val imageData = withContext(Dispatchers.IO) {
                        ImageDataFactory.create(lot.imageUrl)
                    }
                    val image = Image(imageData).setAutoScale(true)
                    imageCell.add(image)
                } catch (e: Exception) {
                    // Add placeholder if the image URL is invalid
                    val inputStream = context.resources.openRawResource(R.drawable.ic_image_placeholder)
                    val placeholderData = ImageDataFactory.create(inputStream.readBytes())
                    val placeholderImage = Image(placeholderData).setAutoScale(true)
                    imageCell.add(placeholderImage)
                }
                table.addCell(imageCell)
            }

            document.add(table)
            document.close()

            Toast.makeText(context, "PDF saved to: ${pdfPath.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to generate PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
