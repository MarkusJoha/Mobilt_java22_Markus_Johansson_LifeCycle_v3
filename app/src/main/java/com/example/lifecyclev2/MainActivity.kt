package com.example.lifecyclev2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val userEmail = currentUser.email

        val userEmailTextView = findViewById<TextView>(R.id.userEmailTextView)
        userEmailTextView.text = "Welcome, $userEmail"

        val submitButton = findViewById<Button>(R.id.submitButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        if (userEmail != null) {
            firestore.collection("answers")
                .document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val answers = document.toObject(Answers::class.java)
                        if (answers != null) {
                            val result = "Answers:\n${answers.answer1}\n${answers.answer2}\n${answers.answer3}\n${answers.answer4}\n${answers.answer5}"
                            resultTextView.text = result
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        baseContext,
                        "Failed to save answers. ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        submitButton.setOnClickListener {
            val input1 = findViewById<EditText>(R.id.editText1).text.toString()
            val input2 = findViewById<EditText>(R.id.editText2).text.toString()
            val input3 = findViewById<EditText>(R.id.editText3).text.toString()
            val input4 = findViewById<EditText>(R.id.editText4).text.toString()
            val input5 = findViewById<EditText>(R.id.editText5).text.toString()

            val answers = Answers(input1, input2, input3, input4, input5)

            if (userEmail != null) {
                firestore.collection("answers")
                    .document(userEmail)
                    .set(answers)
                    .addOnSuccessListener {
                        val result = "Answers:\n$input1\n$input2\n$input3\n$input4\n$input5"
                        resultTextView.text = result
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            baseContext,
                            "Failed to save answers. ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            auth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}