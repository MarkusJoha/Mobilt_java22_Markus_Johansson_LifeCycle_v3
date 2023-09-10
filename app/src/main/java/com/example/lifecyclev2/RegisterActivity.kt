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

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val backBtn: Button = findViewById(R.id.backButton)
        val regBtn: Button = findViewById(R.id.regUser)

        backBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        regBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = findViewById<EditText>(R.id.regFormEmail)
        val password = findViewById<EditText>(R.id.regFormPassword)

        if (email.text.isEmpty() && password.text.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(
                        baseContext,
                        "Successful registration!",
                        Toast.LENGTH_SHORT,
                    ).show()

                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error Occured ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}