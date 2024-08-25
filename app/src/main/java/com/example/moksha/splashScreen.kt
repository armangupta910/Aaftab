package com.example.moksha

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class splashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        // Check if the user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, navigate to the main activity
            val intent = Intent(this, getStarted::class.java)
            startActivity(intent)
            finish() // Finish the splash screen activity
        } else {
            // User is not signed in, navigate to the login activity
            val intent = Intent(this, loginPage::class.java)
            startActivity(intent)
            finish() // Finish the splash screen activity
        }


    }
}