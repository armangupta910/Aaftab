package com.example.moksha

// Define your pose connections
data class PoseConnection(val start: Int, val end: Int)

object PoseConnections {
    val POSE_CONNECTIONS = listOf(
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_SHOULDER, com.google.mlkit.vision.pose.PoseLandmark.LEFT_ELBOW),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_SHOULDER, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_ELBOW),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_ELBOW, com.google.mlkit.vision.pose.PoseLandmark.LEFT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_ELBOW, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_SHOULDER, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_SHOULDER),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_THUMB, com.google.mlkit.vision.pose.PoseLandmark.LEFT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_THUMB, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_INDEX, com.google.mlkit.vision.pose.PoseLandmark.LEFT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_INDEX, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_PINKY, com.google.mlkit.vision.pose.PoseLandmark.LEFT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_PINKY, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_WRIST),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_KNEE, com.google.mlkit.vision.pose.PoseLandmark.LEFT_ANKLE),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_KNEE, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_ANKLE),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_HIP, com.google.mlkit.vision.pose.PoseLandmark.LEFT_KNEE),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_HIP, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_KNEE),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_SHOULDER, com.google.mlkit.vision.pose.PoseLandmark.LEFT_HIP),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.RIGHT_SHOULDER, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_HIP),
        PoseConnection(com.google.mlkit.vision.pose.PoseLandmark.LEFT_HIP, com.google.mlkit.vision.pose.PoseLandmark.RIGHT_HIP),



        // Add more connections as needed
    )
}

