package com.example.moksha

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.pose.PoseLandmark

class LandmarkOverlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private var landmarks: List<PoseLandmark> = emptyList()
    private var imageWidth = 0
    private var imageHeight = 0

    fun setLandmarks(landmarks: List<PoseLandmark>, imageWidth: Int, imageHeight: Int) {
        this.landmarks = landmarks
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (landmarks.isEmpty() || imageWidth == 0 || imageHeight == 0) return

        // Calculate the scale factors
        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight

        // Use the smaller scale factor to maintain aspect ratio
        val scale = minOf(scaleX, scaleY)

        // Calculate the offset to center the image if necessary
        val offsetX = (width - (imageWidth * scale)) / 2
        val offsetY = (height - (imageHeight * scale)) / 2

        for (landmark in landmarks) {
            val cx = (landmark.position.x * scale) + offsetX
            val cy = (landmark.position.y * scale) + offsetY
            canvas.drawCircle(cx, cy, 10f, paint)
        }
    }
}
