package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SIGN : AppCompatActivity() {
    private val databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://midterm-2e9d1-default-rtdb.firebaseio.com\n" +
            "\n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registracia)

        val logbtn = findViewById<Button>(R.id.Logbtn)
        val username = findViewById<EditText>(R.id.Username)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val reppassword = findViewById<EditText>(R.id.reppassword)
        val signUpbtn = findViewById<Button>(R.id.signUpbtn)

        signUpbtn.setOnClickListener{
            val usernametxt = username.text.toString()
            val emailtxt = email.text.toString()
            val passwordtxt = password.text.toString()
            val reppasswordtxt = reppassword.text.toString()

            if (emailtxt.isEmpty() || passwordtxt.isEmpty() || usernametxt.isEmpty() || reppasswordtxt.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            } else if (passwordtxt != reppasswordtxt) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                databaseReference.child("users").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild(emailtxt)) {
                            Toast.makeText(this@SIGN, "This Email is already registered", Toast.LENGTH_SHORT).show()
                        } else {
                            databaseReference.child("users").child(emailtxt).child("username").setValue(usernametxt)
                            databaseReference.child("users").child(emailtxt).child("password").setValue(passwordtxt)
                            Toast.makeText(this@SIGN, "Registration Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SIGN, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
            }
        }

        logbtn.setOnClickListener {
            val Logpage = Intent(this, MainActivity::class.java)
            startActivity(Logpage)
            finish()
        }
    }
}