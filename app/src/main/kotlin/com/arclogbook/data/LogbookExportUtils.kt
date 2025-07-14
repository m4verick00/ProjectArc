package com.arclogbook.data

import android.content.Context
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object LogbookExportUtils {
    fun exportToJson(context: Context, entries: List<LogEntry>): File {
        val json = Json.encodeToString(entries)
        val file = File(context.cacheDir, "ArcLogbookExport.json")
        file.writeText(json)
        return file
    }

    fun exportToPdf(context: Context, entries: List<LogEntry>): File {
        val file = File(context.cacheDir, "ArcLogbookExport.pdf")
        val writer = PdfWriter(file)
        val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(writer)
        val doc = Document(pdfDoc)
        entries.forEach { entry ->
            doc.add(Paragraph("[${entry.type}] ${entry.timestamp}\n${entry.tags}\n${entry.content}\n---"))
        }
        doc.close()
        return file
    }
}
