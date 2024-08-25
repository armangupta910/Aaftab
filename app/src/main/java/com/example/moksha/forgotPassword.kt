package com.example.moksha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class forgotPassword : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailEditText = findViewById(R.id.email)
        sendButton = findViewById(R.id.sendOTP)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        sendButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                checkEmailExistsAndSendOTP(email)
            } else {
                emailEditText.error = "Please enter your email"
            }
        }

    }

    private fun checkEmailExistsAndSendOTP(email: String) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No account found with this Email ID", Toast.LENGTH_SHORT).show()
                } else {
                    sendPasswordResetEmail(email)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Some unknown error has occurred", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,passwordresetemailsent::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Some unknown error has occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

}