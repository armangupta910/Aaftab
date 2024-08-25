package com.example.moksha

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class HeightPickerView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.GRAY
        textSize = 50f
    }
    private var selectedHeight = 160

    fun setSelectedHeight(height: Int) {
        selectedHeight = height
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val heightValues = arrayOf(190, 185, 180, 175, 170, 165, 160)
        val step = height / (heightValues.size - 1)

        for (i in heightValues.indices) {
            val y = i * step
            canvas.drawText("${heightValues[i]} cm", 10f, y.toFloat(), paint)
        }

        paint.color = Color.GREEN
        paint.textSize = 70f
        val selectedY = ((selectedHeight - 160) * step / 10).toFloat()
        canvas.drawText("$selectedHeight cm", width / 2f, selectedY, paint)
        canvas.drawLine(0f, selectedY, width.toFloat(), selectedY, paint)
    }
}
