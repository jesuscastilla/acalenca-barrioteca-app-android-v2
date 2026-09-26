package com.lebeche.barrioteca.ui

import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier,
    cutoutWidth: Float = 0.75f,
    cutoutHeight: Float = 0.35f,
    text: String? = null
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    ) {
        val width = size.width
        val height = size.height

        val rectWidth = width * cutoutWidth
        val rectHeight = height * cutoutHeight

        val left = (width - rectWidth) / 2
        val top = (height - rectHeight) / 2
        val right = left + rectWidth
        val bottom = top + rectHeight

        // Draw the semi-transparent background
        drawRect(
            color = Color.Black.copy(alpha = 0.6f),
            size = size
        )

        // Create a transparent cutout in the middle
        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(24f, 24f),
            blendMode = BlendMode.Clear
        )

        // Draw the white frame with corners
        val cornerLength = 80f
        val strokeWidth = 10f
        val color = Color.White
        val path = Path().apply {
            // Top Left
            moveTo(left, top + cornerLength)
            lineTo(left, top)
            lineTo(left + cornerLength, top)

            // Top Right
            moveTo(right - cornerLength, top)
            lineTo(right, top)
            lineTo(right, top + cornerLength)

            // Bottom Right
            moveTo(right, bottom - cornerLength)
            lineTo(right, bottom)
            lineTo(right - cornerLength, bottom)

            // Bottom Left
            moveTo(left + cornerLength, bottom)
            lineTo(left, bottom)
            lineTo(left, bottom - cornerLength)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth)
        )

        // Draw optional text below the scanner
        if (!text.isNullOrBlank()) {
            val textLayoutResult = textMeasurer.measure(
                text = text,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                constraints = Constraints(maxWidth = (width - 48f).toInt().coerceAtLeast(1)),
                softWrap = true,
                maxLines = 2
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = (width - textLayoutResult.size.width) / 2f,
                    y = bottom + 60f
                )
            )
        }
    }
}