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
        comments: String,
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
        val title = Paragraph("Meeting ID - ${meeting.id}").setFont(titleFont)
        document.add(title)

        val date = Paragraph("Date of event: ${getCurrentDate()}").setFont(titleFont)
        document.add(date)

        val institutionName = Paragraph("Institution name: ${meeting.address.officeName}").setFont(titleFont)
        document.add(institutionName)

        val institutionAddress = Paragraph("Institution address: ${meeting.address.lineOne}").setFont(titleFont)
        document.add(institutionAddress)

        val physicianName = Paragraph("Physician name: ${meeting.address.physicianName}").setFont(titleFont)
        document.add(physicianName)

        val coordinatorFood = Paragraph(
            "Did Coordinator Consume Food - " +
                    if (meeting.coordinatorWillConsumeFood) "Yes" else "No"
        ).setFont(normalFont)
        document.add(coordinatorFood)

        val attendeesTitle = Paragraph("Attendees:").setFont(titleFont)
        document.add(attendeesTitle)

        val attendeesTableHeaders = listOf(
            "",
            "FIRST NAME",
            "LAST NAME",
            "PROFESSIONAL \n DESIGNATION",
            "SIGNATURE",
            "CONSUME FOOD"
        )

        // Create table for attendees
        val attendeesTable = Table(floatArrayOf(20f, 50f, 50f, 50f, 30f, 20f)).useAllAvailableWidth()
        attendeesTableHeaders.forEach {
            attendeesTable.addHeaderCell(it)
        }
        meeting.attendee.forEachIndexed { index, attendee ->
            attendeesTable.addCell(Cell().add(Paragraph("${index + 1}")))
            attendeesTable.addCell(Cell().add(Paragraph(attendee.attendeeFirstName)))
            attendeesTable.addCell(Cell().add(Paragraph(attendee.attendeeLastName)))
            attendeesTable.addCell(Cell().add(Paragraph(attendee.attendeeProfessionalDesignation)))
            attendeesTable.addCell(Cell().add(Paragraph("")))
            attendeesTable.addCell(Cell().add(Paragraph(if (attendee.attendeeWillConsumeFood) "Yes" else "No")))
        }
        document.add(attendeesTable)

        val meetingStatisticsTitle = Paragraph("Meeting Statistics:").setFont(titleFont)
        document.add(meetingStatisticsTitle)

        meetingStatistics.forEach { (key, value) ->
            val item = Paragraph("$key - $value").setFont(normalFont)
            document.add(item)
        }

        val generalComments = Paragraph("General Comments: $comments").setFont(titleFont)
        document.add(generalComments)

        document.close()
        pdfDocument.close()
        pdfWriter.close()

        Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show()
    }

}