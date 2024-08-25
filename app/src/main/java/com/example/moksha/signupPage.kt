package com.example.moksha

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class signupPage : AppCompatActivity() {

    private lateinit var passwordEditText: EditText
    private var isPasswordVisible: Boolean = false
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        findViewById<TextView>(R.id.gotoSignIn).setOnClickListener {
            startActivity(Intent(this,loginPage::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.goBackImage).setOnClickListener {
            startActivity(Intent(this,beginOptionPage::class.java))
            finish()
        }

        val genderDropdown: AutoCompleteTextView = findViewById(R.id.gender)
        // Define the options for the dropdown
        val genders = listOf("Male", "Female", "Other")

        // Create an ArrayAdapter using the gender options
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)

        // Set the adapter to the AutoCompleteTextView
        genderDropdown.setAdapter(adapter)

        // Show the dropdown when the field is clicked
        genderDropdown.setOnClickListener {
            genderDropdown.setBackgroundResource(R.drawable.roundbuttonbglogin)
            genderDropdown.showDropDown()
        }

        passwordEditText = findViewById<EditText>(R.id.password)

        passwordEditText.setOnTouchListener { _, event ->
            val DRAWABLE_END = 2
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }


        findViewById<EditText>(R.id.name).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the text within start to start + before
                // is about to be replaced with new text with length after.
                // You can perform operations before text changes here if needed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within s, the text has been changed.
                // Perform operations as the text is being changed.
                findViewById<EditText>(R.id.name).setBackgroundResource(R.drawable.roundbuttonbglogin)
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that somewhere within s, the text has been changed.
                // Perform operations after the text has been changed if needed.
            }
        })

        findViewById<EditText>(R.id.email).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the text within start to start + before
                // is about to be replaced with new text with length after.
                // You can perform operations before text changes here if needed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within s, the text has been changed.
                // Perform operations as the text is being changed.
                findViewById<EditText>(R.id.email).setBackgroundResource(R.drawable.roundbuttonbglogin)
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that somewhere within s, the text has been changed.
                // Perform operations after the text has been changed if needed.
            }
        })

        findViewById<EditText>(R.id.password).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the text within start to start + before
                // is about to be replaced with new text with length after.
                // You can perform operations before text changes here if needed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within s, the text has been changed.
                // Perform operations as the text is being changed.
                findViewById<EditText>(R.id.password).setBackgroundResource(R.drawable.roundbuttonbglogin)
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that somewhere within s, the text has been changed.
                // Perform operations after the text has been changed if needed.
            }
        })



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleSignInButton: CardView = findViewById(R.id.googleSignUp)
        googleSignInButton.setOnClickListener {
            signOutAndSignInWithGoogle()
        }




        //Registering the User
        val register:Button = findViewById(R.id.register)
        register.setOnClickListener {
            val name:String = findViewById<EditText>(R.id.name).text.toString()
            val email:String = findViewById<EditText>(R.id.email).text.toString()
            val gender:String = findViewById<AutoCompleteTextView>(R.id.gender).text.toString()
            val password:String = findViewById<EditText>(R.id.password).text.toString()

            var ref = 0

            if(name == ""){
                ref = 1
                findViewById<EditText>(R.id.name).setBackgroundResource(R.drawable.wrongpassword)
            }
            if(email == ""){
                ref = 1
                findViewById<EditText>(R.id.email).setBackgroundResource(R.drawable.wrongpassword)
            }
            if(gender == ""){
                ref = 1
                findViewById<AutoCompleteTextView>(R.id.gender).setBackgroundResource(R.drawable.wrongpassword)
            }
            if(password == ""){
                ref = 1
                findViewById<EditText>(R.id.password).setBackgroundResource(R.drawable.wrongpassword)
            }

            if(ref == 1){
                Toast.makeText(this,"Please fill up all the fields to proceed",Toast.LENGTH_SHORT).show()
            }


            if(ref == 0){
                registerUser(name,email,gender,password)
            }
        }

    }

    private fun registerUser(name: String, email: String, gender: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    val uid = user?.uid

                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "gender" to gender
                    )

                    if (uid != null) {
                        db.collection("users").document(uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,getStarted::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Registration failed
                    if(task.exception?.message.toString() == "The email address is already in use by another account."){
                        Toast.makeText(this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Some unknown eroor has ocurred. Please try after some time", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun signOutAndSignInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener(this) {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null) {
                    val emailEditText: EditText = findViewById(R.id.email)
                    emailEditText.setText(account.email)
                    emailEditText.isFocusable = false
                    emailEditText.isFocusableInTouchMode = false
                    emailEditText.isClickable = false
                    Toast.makeText(this,"Please fill up your other details and then register.",Toast.LENGTH_SHORT).show()
                    emailEditText.setTextColor(resources.getColor(R.color.grey))
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Google Sign in Failed :- ${e.message}")
            }
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password_resized, 0)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password_resized, 0)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)  // Move cursor to the end of text
    }
}