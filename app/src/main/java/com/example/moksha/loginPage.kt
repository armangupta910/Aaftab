package com.example.moksha

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
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

class loginPage : AppCompatActivity() {
    private var isPasswordVisible: Boolean = false
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        findViewById<TextView>(R.id.gotoSignUp).setOnClickListener {
            startActivity(Intent(this,signupPage::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.goBackImage).setOnClickListener {
            startActivity(Intent(this,beginOptionPage::class.java))
            finish()
        }

        findViewById<TextView>(R.id.forgot).setOnClickListener {
            startActivity(Intent(this,forgotPassword::class.java))
        }

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        findViewById<EditText>(R.id.password).setOnTouchListener { _, event ->
            val DRAWABLE_END = 2
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= (findViewById<EditText>(R.id.password).right - findViewById<EditText>(R.id.password).compoundDrawables[DRAWABLE_END].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

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

        val googleSignInButton: CardView = findViewById(R.id.googleSignIn)
        googleSignInButton.setOnClickListener {
            signOutAndSignInWithGoogle()
        }


        //Registering the User
        val signIn: Button = findViewById(R.id.signin)
        signIn.setOnClickListener {
            val email:String = findViewById<EditText>(R.id.email).text.toString()
            val password:String = findViewById<EditText>(R.id.password).text.toString()

            var ref = 0

            if(email == ""){
                ref = 1
                findViewById<EditText>(R.id.email).setBackgroundResource(R.drawable.wrongpassword)
            }
            if(password == ""){
                ref = 1
                findViewById<EditText>(R.id.password).setBackgroundResource(R.drawable.wrongpassword)
            }

            if(ref == 1){
                Toast.makeText(this,"Please fill up all the fields to proceed",Toast.LENGTH_SHORT).show()
            }


            if(ref == 0){
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    if (uid != null) {
                        db.collection("users").document(uid)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val name = document.getString("name")
                                    val gender = document.getString("gender")
                                    Toast.makeText(this, "Welcome back, $name!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this,getStarted::class.java))
                                    finish()
                                    // You can navigate to the next screen or perform any desired actions here
                                } else {
                                    Toast.makeText(this, "Email ID is not regsitered", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Some unknown error has ocurred ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show()
                    findViewById<EditText>(R.id.email).setBackgroundResource(R.drawable.wrongpassword)
                    findViewById<EditText>(R.id.password).setBackgroundResource(R.drawable.wrongpassword)
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
                    Toast.makeText(this,"Please fill up your other details and then register.",
                        Toast.LENGTH_SHORT).show()
                    emailEditText.setTextColor(resources.getColor(R.color.grey))
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG,"Google Sign in Failed :- ${e.message}")
            }
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            findViewById<EditText>(R.id.password).transformationMethod = PasswordTransformationMethod.getInstance()
            findViewById<EditText>(R.id.password).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password_resized, 0)
        } else {
            findViewById<EditText>(R.id.password).transformationMethod = HideReturnsTransformationMethod.getInstance()
            findViewById<EditText>(R.id.password).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password_resized, 0)
        }
        isPasswordVisible = !isPasswordVisible
        findViewById<EditText>(R.id.password).setSelection(findViewById<EditText>(R.id.password).text.length)  // Move cursor to the end of text
    }
}