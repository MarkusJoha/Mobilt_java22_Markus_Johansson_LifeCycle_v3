package com.example.lifecyclev2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val regBtn: Button = findViewById(R.id.registerButton)
        val loginBtn: Button = findViewById(R.id.loginButton)

        regBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            performLogin()
        }

    }

    private fun performLogin() {
        val email: EditText = findViewById(R.id.emailLogin)
        val password: EditText = findViewById(R.id.passwordLogin)

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userEmail = user?.email ?: ""
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userEmail", userEmail)
                    startActivity(intent)

                    Toast.makeText(baseContext, "Successful login!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext,
                    "Authentication failed. ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}