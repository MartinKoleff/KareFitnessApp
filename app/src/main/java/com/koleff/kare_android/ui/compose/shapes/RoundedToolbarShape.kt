package com.koleff.kare_android.ui.compose.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class RoundedToolbarShape(val hasTopOutline: Boolean = true) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawRoundedToolbarSquare(size)
        )
    }

    private fun drawRoundedToolbarSquare(size: Size): Path {
        val screenWidth = size.width
        val screenHeight = size.height
        val cornerRadius = screenWidth / 3

        val rect = Rect(
            left = screenWidth - 2 * cornerRadius,
            top = screenHeight - 2 * cornerRadius,
            right = screenWidth,
            bottom = screenHeight
        )

        val path = Path().apply {
            if(hasTopOutline) moveTo(0f, 0f) else moveTo(screenWidth, 0f)
            if(hasTopOutline) lineTo(screenWidth, 0f)
            lineTo(screenWidth, screenHeight - cornerRadius)
            arcTo(
                rect = rect,
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadius, screenHeight)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = screenHeight - 2 * cornerRadius,
                    right = cornerRadius * 2,
                    bottom = screenHeight
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            if(hasTopOutline) close() else lineTo(0f, 0f)
        }

        return path
    }
}