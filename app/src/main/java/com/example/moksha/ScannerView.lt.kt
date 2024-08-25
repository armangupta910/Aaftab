package com.example.moksha

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class ScannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintRect = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val paintLine = Paint().apply {
        color = Color.WHITE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private var posY = 1f
    private val step = 10f
    private val margin = 50f // margin for the rectangle from the view edges
    private val cornerRadius = 60f // radius for the rounded corners
    private val lineMargin = 20f

    init {
        // Update the position of the scanner line every 16ms
        post(object : Runnable {
            override fun run() {
                posY += step
                if (posY > height - margin - lineMargin) {
                    posY = margin + lineMargin
                }
                invalidate()
                postDelayed(this, 16)
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the rounded rectangle
        val left = margin
        val top = margin
        val right = width - margin
        val bottom = height - margin
        val rect = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paintRect)

        // Draw the scanner line within the rectangle with margins
        canvas.drawLine(left + lineMargin, posY, right - lineMargin, posY, paintLine)
    }
}
