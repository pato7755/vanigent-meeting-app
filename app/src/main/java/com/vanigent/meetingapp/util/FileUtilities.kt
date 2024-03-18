package com.vanigent.meetingapp.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.itextpdf.io.font.FontConstants
import com.itextpdf.kernel.font.PdfFontFactory.createFont
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.vanigent.meetingapp.domain.model.Meeting
import com.vanigent.meetingapp.util.DateUtilities.getCurrentDate
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

object FileUtilities {

    fun storeBitmapImage(
        context: Context,
        fileName: String,
        imageBitmap: Bitmap
    ): String {

        // Access the application's internal storage directory
        val directory = File(context.filesDir, getCurrentDate())
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        return try {
            val fos = FileOutputStream(file)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            Timber.d(file.relativeTo(context.filesDir).path)
            file.relativeTo(context.filesDir).path
        } catch (e: Exception) {
            println("exception - ${e.localizedMessage}")
            e.printStackTrace()
            ""
        }

    }

    fun generatePDF(
        context: Context,
        filename: String,
        meeting: Meeting,
        meetingStatistics: Map<String, String>
    ) {
        val file = File(context.filesDir, filename)
        val outputStream = FileOutputStream(file)
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument, PageSize.A4)

        val titleFont = createFont(FontConstants.HELVETICA_BOLD)
        val normalFont = createFont(FontConstants.HELVETICA)

        // Write Meeting data onto the page
        val title = Paragraph("Meeting ID - 29").setFont(titleFont)
        document.add(title)

        val date = Paragraph("Date of event: ${getCurrentDate()}").setFont(titleFont)
        document.add(date)

        val location = Paragraph("Location - ${meeting.officeLocation}").setFont(normalFont)
        document.add(location)

        val coordinatorFood = Paragraph(
            "Coordinator Will Consume Food - " +
                    if (meeting.coordinatorWillConsumeFood) "Yes" else "No"
        ).setFont(normalFont)
        document.add(coordinatorFood)

        val attendeesTitle = Paragraph("Attendees:").setFont(titleFont)
        document.add(attendeesTitle)

        val attendeesTableHeaders = listOf(
            "FIRST NAME",
            "LAST NAME",
            "PROFESSIONAL DESIGNATION",
            "SIGNATURE",
            "CONSUME FOOD"
        )

        // Create table for attendees
        val attendeesTable = Table(floatArrayOf(20f, 50f, 50f, 30f, 20f)).useAllAvailableWidth()
        attendeesTableHeaders.forEach {
            attendeesTable.addHeaderCell(it)
        }
        meeting.attendee.forEachIndexed { index, attendee ->
            attendeesTable.addCell(Cell().add(Paragraph("${index + 1}")))
            attendeesTable.addCell(Cell().add(Paragraph(attendee.attendeeFirstName)))
            attendeesTable.addCell(Cell().add(Paragraph(attendee.attendeeLastName)))
            attendeesTable.addCell(Cell().add(Paragraph(attendee.attendeeProfessionalDesignation)))
            attendeesTable.addCell(Cell().add(Paragraph(if (attendee.attendeeWillConsumeFood) "Yes" else "No")))
        }
        document.add(attendeesTable)

        val meetingStatisticsTitle = Paragraph("Meeting Statistics:").setFont(titleFont)
        document.add(meetingStatisticsTitle)

        meetingStatistics.forEach { (key, value) ->
            val item = Paragraph("$key - $value").setFont(normalFont)
            document.add(item)
        }


        // Create table for receipts
//        val receiptsTable = Table(floatArrayOf(200f, 100f)).useAllAvailableWidth()
//        meeting.receipt.forEachIndexed { index, receipt ->
//            val vendorName = receipt.receiptItems["VENDOR NAME"] ?: ""
//            val total = receipt.receiptItems["TOTAL"] ?: ""
//            receiptsTable.addCell(Cell().add(Paragraph("${index + 1}. Vendor Name: $vendorName")))
//            receiptsTable.addCell(Cell().add(Paragraph("Total: $total")))
//        }
//        document.add(receiptsTable)

        document.close()
        pdfDocument.close()
        pdfWriter.close()

        Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show()
    }

    fun generateITextPDF(context: Context, filename: String, meeting: Meeting) {
        val file = File(context.filesDir, filename)
        val outputStream = FileOutputStream(file)
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)
        val document = com.itextpdf.layout.Document(pdfDocument)

        val titleFont = createFont(FontConstants.HELVETICA_BOLD)
        val normalFont = createFont(FontConstants.HELVETICA)

        // Write Meeting data onto the page
        val title = Paragraph("Meeting ID - 28").setFont(titleFont)
        document.add(title)

        val location = Paragraph("Location - ${meeting.officeLocation}").setFont(normalFont)
        document.add(location)

        val coordinatorFood =
            Paragraph("Coordinator Will Consume Food - ${if (meeting.coordinatorWillConsumeFood) "Yes" else "No"}").setFont(
                normalFont
            )
        document.add(coordinatorFood)

        val attendeesTitle = Paragraph("Attendees:").setFont(titleFont)
        document.add(attendeesTitle)
        meeting.attendee.forEachIndexed { index, attendee ->
            val attendeeText =
                Paragraph("${index + 1}. ${attendee.attendeeFirstName} ${attendee.attendeeLastName}").setFont(
                    normalFont
                )
            document.add(attendeeText)
            val designationText =
                Paragraph("   Professional Designation: ${attendee.attendeeProfessionalDesignation}").setFont(
                    normalFont
                )
            document.add(designationText)
            val consumeFoodText =
                Paragraph("   Will Consume Food: ${if (attendee.attendeeWillConsumeFood) "Yes" else "No"}").setFont(
                    normalFont
                )
            document.add(consumeFoodText)
        }

        val receiptsTitle = Paragraph("Receipts:").setFont(titleFont)
        document.add(receiptsTitle)
        meeting.receipt.forEachIndexed { index, receipt ->
            val vendorName = receipt.receiptItems["VENDOR NAME"]
            val total = receipt.receiptItems["TOTAL"]
            val receiptText =
                Paragraph("${index + 1}. Vendor Name: $vendorName").setFont(normalFont)
            document.add(receiptText)
            val totalText = Paragraph("   Total: $total").setFont(normalFont)
            document.add(totalText)
        }

        document.close()
        pdfDocument.close()
        pdfWriter.close()

        Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show()
    }


}