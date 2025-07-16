package com.arclogbook.report

import android.content.Context
import java.io.File

object ReportGenerator {
    fun generatePdfReport(context: Context, data: String, file: File) {
        file.writeText("PDF Report: $data")
    }
    fun generateMarkdownReport(context: Context, data: String, file: File) {
        file.writeText("# Investigation Report\n$data")
    }
}
