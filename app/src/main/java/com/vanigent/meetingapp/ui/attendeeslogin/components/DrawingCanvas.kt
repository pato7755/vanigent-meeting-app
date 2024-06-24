package com.vanigent.meetingapp.ui.attendeeslogin.components

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanigent.meetingapp.util.Constants.FIVE_HUNDRED


@Composable
fun DrawingCanvas(
    lines: SnapshotStateList<Line>,
    signatureBitmap: ImageBitmap?,
    onSignatureChanged: (List<Line>) -> Unit,
    onSubmitButtonPressed: (ImageBitmap) -> Unit
) {

    val rememberedLines = rememberUpdatedState(lines)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(true) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val line = Line(
                        start = change.position - dragAmount,
                        end = change.position
                    )

                    rememberedLines.value.add(line)

                    onSignatureChanged(rememberedLines.value)
                }
            }
    ) {
        lines.forEach { line ->
            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }

    val width = FIVE_HUNDRED
    val height = FIVE_HUNDRED

    val bitmap = signatureBitmap ?: LinesToBitmap(rememberedLines.value, width, height)
    onSubmitButtonPressed(bitmap)
}

@Composable
fun LinesToBitmap(lines: List<Line>, width: Int, height: Int): ImageBitmap {
    val density = LocalDensity.current.density
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    lines.forEach { line ->
        canvas.drawLine(
            line.start.x,
            line.start.y,
            line.end.x,
            line.end.y,
            Paint().apply {
                strokeWidth = with(LocalDensity.current) { line.strokeWidth.toPx() / density }
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
            }
        )
    }

    return bitmap.asImageBitmap()
}

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWidth: Dp = 2.dp
)