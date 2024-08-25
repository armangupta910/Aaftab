package com.example.moksha

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import android.graphics.Matrix
import android.graphics.PointF
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import com.google.mlkit.vision.pose.PoseLandmark
import java.lang.Math.toDegrees
import java.util.Locale
import kotlin.math.abs
import kotlin.math.atan2


class demo : AppCompatActivity() {
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    // Base pose detector with streaming frames, when depending on the pose-detection sdk
    val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    val poseDetector = PoseDetection.getClient(options)

    var canvas: Canvas? = null
    lateinit var mPaint:Paint

    lateinit var displayOverlay:com.example.moksha.Display
    lateinit var poseName:String

    private fun calculateAngle(point1: PointF, point2: PointF, point3: PointF): Double {
        val angle = toDegrees(
            atan2((point3.y - point2.y).toDouble(), (point3.x - point2.x).toDouble()) -
                    atan2((point1.y - point2.y).toDouble(), (point1.x - point2.x).toDouble())
        )
        var x = abs(angle)
        if(x > 180){
            return 360-x
        }
        return abs(angle)
    }

    private fun classifyPlankPose(landmarks: List<PoseLandmark>): String {
        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        Log.d(TAG,"Left Shoulder Angle ${leftShoulderAngle.toString()}")
        Log.d(TAG,"Right Shoulder Angle ${rightShoulderAngle.toString()}")
        Log.d(TAG,"Left Knee Angle ${leftKneeAngle.toString()}")
        Log.d(TAG,"Right Knee Angle ${rightKneeAngle.toString()}")
        Log.d(TAG,"Left Hip Angle ${leftHipAngle.toString()}")
        Log.d(TAG,"Right Hip Angle ${rightHipAngle.toString()}")
        Log.d(TAG,"Left Elbow Angle ${leftElbowAngle.toString()}")
        Log.d(TAG,"Right Elbow Angle ${rightElbowAngle.toString()}")


        if (50 <= leftShoulderAngle && leftShoulderAngle <= 105 &&
            50 <= rightShoulderAngle && rightShoulderAngle <= 105 &&
            80 <= leftKneeAngle && leftKneeAngle <= 180 &&
            80 < rightKneeAngle && rightKneeAngle <= 180 &&
            90 <= leftHipAngle && leftHipAngle <= 180 &&
            90 <= rightHipAngle && rightHipAngle <= 180 &&
            90 <= leftElbowAngle && leftElbowAngle <= 180 &&
            90 <= rightElbowAngle && rightElbowAngle <= 180) {
            return "Plank"
        }
        return "Unknown Pose"
    }

    private fun classifyCobraPose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        Log.d(TAG,"Left Knee Angle ${leftKneeAngle.toString()}")
        Log.d(TAG,"Right Knee Angle ${rightKneeAngle.toString()}")
        Log.d(TAG,"Left Elbow Angle ${leftElbowAngle.toString()}")
        Log.d(TAG,"Right Elbow Angle ${rightElbowAngle.toString()}")
        Log.d(TAG,"Left Hip Angle ${leftHipAngle.toString()}")
        Log.d(TAG,"Right Hip Angle ${rightHipAngle.toString()}")
        Log.d(TAG,"Left Shoulder Angle ${leftShoulderAngle.toString()}")
        Log.d(TAG,"Right Shoulder Angle ${rightShoulderAngle.toString()}")



        if (leftKneeAngle in 110.0..180.0 &&
            110 <= rightKneeAngle && rightKneeAngle <= 180 &&
            75 <= leftElbowAngle && leftElbowAngle <= 180 &&
            75 <= rightElbowAngle && rightElbowAngle <= 180 &&
            80 <= leftHipAngle && leftHipAngle <= 180 &&
            80 <= rightHipAngle && rightHipAngle <= 180 &&
            7 <= leftShoulderAngle && leftShoulderAngle <= 180 &&
            7 <= rightShoulderAngle && rightShoulderAngle <= 180) {
            return "Cobra"
        }
        return "Unknown Pose"
    }

    private fun classifyCobblerPose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        if (11 <= leftKneeAngle && leftKneeAngle <= 100 &&
            11 <= rightKneeAngle && rightKneeAngle <= 100 &&
            80 <= leftElbowAngle && leftElbowAngle <= 180 &&
            80 <= rightElbowAngle && rightElbowAngle <= 180 &&
            4 <= leftHipAngle && leftHipAngle <= 170 &&
            1 <= rightHipAngle && rightHipAngle <= 173 &&
            0 <= leftShoulderAngle && leftShoulderAngle <= 180 &&
            0 <= rightShoulderAngle && rightShoulderAngle <= 179) {
            return "Cobbler"
        }
        return "Unknown Pose"
    }

    fun classifyExtendedPose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        if (40 <= leftKneeAngle && leftKneeAngle <= 180 &&
            40 <= rightKneeAngle && rightKneeAngle <= 180 &&
            90 <= leftElbowAngle && leftElbowAngle <= 180 &&
            90 <= rightElbowAngle && rightElbowAngle <= 180 &&
            10 <= leftHipAngle && leftHipAngle <= 180 &&
            10 <= rightHipAngle && rightHipAngle <= 180 &&
            50 <= leftShoulderAngle && leftShoulderAngle <= 180 &&
            50 <= rightShoulderAngle && rightShoulderAngle <= 180) {
            return "Extended"
        }
        return "Unknown Pose"
    }

    fun classifyDownwardPose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        if (40 <= leftKneeAngle && leftKneeAngle <= 180 &&
            124 <= rightKneeAngle && rightKneeAngle <= 180 &&
            98 <= leftElbowAngle && leftElbowAngle <= 180 &&
            10 <= rightElbowAngle && rightElbowAngle <= 180 &&
            22 <= leftHipAngle && leftHipAngle <= 173 &&
            13 <= rightHipAngle && rightHipAngle <= 171 &&
            12 <= leftShoulderAngle && leftShoulderAngle <= 180 &&
            13 <= rightShoulderAngle && rightShoulderAngle <= 180) {
            return "Downward"
        }

        return "Unknown Pose"
    }

    fun classifyTrianglePose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        Log.d(TAG,"Left Knee Angle ${leftKneeAngle.toString()}")
        Log.d(TAG,"Right Knee Angle ${rightKneeAngle.toString()}")
        Log.d(TAG,"Left Elbow Angle ${leftElbowAngle.toString()}")
        Log.d(TAG,"Right Elbow Angle ${rightElbowAngle.toString()}")
        Log.d(TAG,"Left Hip Angle ${leftHipAngle.toString()}")
        Log.d(TAG,"Right Hip Angle ${rightHipAngle.toString()}")
        Log.d(TAG,"Left Shoulder Angle ${leftShoulderAngle.toString()}")
        Log.d(TAG,"Right Shoulder Angle ${rightShoulderAngle.toString()}")

        if (80 <= leftKneeAngle && leftKneeAngle <= 180 &&
            80 <= rightKneeAngle && rightKneeAngle <= 180 &&
            100 <= leftElbowAngle && leftElbowAngle <= 180 &&
            100 <= rightElbowAngle && rightElbowAngle <= 180 &&
            60 <= leftShoulderAngle && leftShoulderAngle <= 180 &&
            80 <= rightShoulderAngle && rightShoulderAngle <= 180 &&
            (20 <= leftHipAngle && leftHipAngle <= 60 || 20 <= rightHipAngle && rightHipAngle <= 60) &&
            (120 <= leftHipAngle && leftHipAngle <= 180 || 120 <= rightHipAngle && rightHipAngle <= 180)) {
            return "Triangle"
        }

        return "Unknown Pose"
    }

    fun classifyWarrior1Pose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        // Check if the angles match the Warrior I Pose criteria
        if (70 <= leftKneeAngle && leftKneeAngle <= 180 &&
            70 <= rightKneeAngle && rightKneeAngle <= 180 &&
            103 <= leftElbowAngle && leftElbowAngle <= 180 &&
            103 <= rightElbowAngle && rightElbowAngle <= 180 &&
            80 <= leftShoulderAngle && leftShoulderAngle <= 190 &&
            80 <= rightShoulderAngle && rightShoulderAngle <= 190 &&
            60 <= leftHipAngle && leftHipAngle <= 160 &&
            60 <= rightHipAngle && rightHipAngle <= 160) {
            return "Warrior 1"
        }

        return "Unknown Pose"
    }

    fun classifyWarrior2Pose(landmarks: List<PoseLandmark>): String {
        val leftKneeAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position,
            landmarks[PoseLandmark.LEFT_ANKLE].position
        )

        val rightKneeAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position,
            landmarks[PoseLandmark.RIGHT_ANKLE].position
        )

        val leftElbowAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_WRIST].position
        )

        val rightElbowAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position,
            landmarks[PoseLandmark.RIGHT_WRIST].position
        )

        val leftShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_ELBOW].position,
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position
        )

        val rightShoulderAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_ELBOW].position
        )

        val leftHipAngle = calculateAngle(
            landmarks[PoseLandmark.LEFT_SHOULDER].position,
            landmarks[PoseLandmark.LEFT_HIP].position,
            landmarks[PoseLandmark.LEFT_KNEE].position
        )

        val rightHipAngle = calculateAngle(
            landmarks[PoseLandmark.RIGHT_SHOULDER].position,
            landmarks[PoseLandmark.RIGHT_HIP].position,
            landmarks[PoseLandmark.RIGHT_KNEE].position
        )

        // Check if both arms are straight
        if (leftElbowAngle in 165.0..195.0 && rightElbowAngle in 165.0..195.0) {
            // Check if shoulders are at the required angle
            if (leftShoulderAngle in 80.0..110.0 && rightShoulderAngle in 80.0..110.0) {
                // Check if one leg is straight and the other leg is bent at the required angle
                if ((leftKneeAngle in 165.0..195.0 && rightKneeAngle in 90.0..120.0) ||
                    (rightKneeAngle in 165.0..195.0 && leftKneeAngle in 90.0..120.0)) {
                    return "Warrior 2"
                }
            }
        }

        return "Unknown Pose"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        poseName = intent.getStringExtra("poseName").toString()
        Toast.makeText(this,"Pose :- ${poseName}",Toast.LENGTH_SHORT).show()

        previewView = findViewById(R.id.previewView)
        mPaint = Paint()
        mPaint.setColor(Color.GREEN)
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.strokeWidth = 3F
        displayOverlay = findViewById(R.id.displayOverlay)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            // enable the following line if RGBA output is needed.
             .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
//            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()



        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
            // insert your code here.

//            var bytebuffer: ByteBuffer = imageProxy.image!!.planes[0].buffer
//            bytebuffer.rewind()
//
//            var bitmap: Bitmap =
//                Bitmap.createBitmap(imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888)
//            bitmap.copyPixelsToBuffer(bytebuffer)

            val mediaImage = imageProxy.image

            if (mediaImage != null) {

                val planes = mediaImage.planes
                val buffer = planes[0].buffer


                val bitmap = Bitmap.createBitmap(
                    imageProxy.width,imageProxy.height, Bitmap.Config.ARGB_8888
                )
                bitmap.copyPixelsFromBuffer(buffer)

                val matrix = Matrix()
                matrix.postRotate(90f)
//                matrix.postScale(-1f, 1f)
                val rotatedBitmap: Bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    imageProxy.width,
                    imageProxy.height,
                    matrix,
                    false
                )


                canvas = Canvas(rotatedBitmap)

                val image = InputImage.fromBitmap(bitmap, imageProxy.imageInfo.rotationDegrees)
                poseDetector.process(image)
                    .addOnSuccessListener { pose ->
                        // Task completed successfully
                        // Process the pose results
                        for (demo in pose.allPoseLandmarks) {
                            Log.d(TAG, "Pose Data :- ${demo.position.x} ${demo.position.y}")
                            canvas!!.drawCircle(demo.position.x,demo.position.y, 2F,mPaint)
                        }
                        mPaint.setColor(Color.RED)
                        mPaint.strokeWidth = 2F
                        for (connection in PoseConnections.POSE_CONNECTIONS) {
                            val startLandmark = pose.getPoseLandmark(connection.start)
                            val endLandmark = pose.getPoseLandmark(connection.end)
                            if (startLandmark != null && endLandmark != null) {
                                canvas!!.drawLine(
                                    startLandmark.position.x, startLandmark.position.y,
                                    endLandmark.position.x, endLandmark.position.y,
                                    mPaint
                                )
                            }
                        }

                        val landmarks = pose.allPoseLandmarks
                        if(landmarks.size > 0){
                            Log.d(TAG,"GHUSA")
                            if(poseName == "Plank") {
                                val detectedPose = classifyPlankPose(landmarks)
                                if (detectedPose == "Plank") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }
                            else if(poseName == "Cobra") {
                                val detectedPose = classifyCobraPose(landmarks)
                                if (detectedPose == "Cobra") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }
                            else if(poseName == "Cobbler") {
                                val detectedPose = classifyCobblerPose(landmarks)
                                if (detectedPose == "Cobbler") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }

                            else if(poseName == "Extended") {
                                val detectedPose = classifyExtendedPose(landmarks)
                                if (detectedPose == "Extended") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }

                            else if(poseName == "Downward") {
                                val detectedPose = classifyDownwardPose(landmarks)
                                if (detectedPose == "Downward") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }

                            else if(poseName == "Triangle") {
                                val detectedPose = classifyTrianglePose(landmarks)
                                if (detectedPose == "Triangle") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }

                            else if(poseName == "Warrior 1") {
                                val detectedPose = classifyWarrior1Pose(landmarks)
                                if (detectedPose == "Warrior 1") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }

                            else if(poseName == "Warrior 2") {
                                val detectedPose = classifyWarrior2Pose(landmarks)
                                if (detectedPose == "Warrior 2") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.GREEN)
                                } else if (detectedPose == "Unknown Pose") {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.RED)
                                } else {
                                    findViewById<View>(R.id.check).setBackgroundColor(Color.WHITE)
                                }

                                Log.d(TAG, "Detected Pose: $poseName")
                            }

                        }


                        mPaint.setColor(Color.GREEN)
                        mPaint.strokeWidth = 3F

                        displayOverlay.getBitmap(rotatedBitmap)
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        Log.d(TAG, "ERROR :- ${e.message}")

                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }

            }
            // after done, release the ImageProxy object
        }

        preview.setSurfaceProvider(previewView.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)

    }

}