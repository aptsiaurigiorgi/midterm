package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private val databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://midterm-2e9d1-default-rtdb.firebaseio.com")
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password
        val loginBtn = binding.logbtn

        loginBtn.setOnClickListener {
            val emailtxt = email.text.toString()
            val passwordtxt = password.text.toString()

            if (emailtxt.isEmpty() || passwordtxt.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please fill all required field",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                databaseReference.child("users").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(emailtxt)) {
                            val getPassword = snapshot.child(emailtxt).child("password")
                                .getValue(String::class.java)

                            if (getPassword == passwordtxt) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Successfully logged in",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Wrong password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
            }
        }

        val goToRegBtn = binding.signUPbtn

        goToRegBtn.setOnClickListener {
            val regPage = Intent(this, SIGN::class.java)
            startActivity(regPage)
            finish()
        }

        replaceFragment(Home())

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.profile -> replaceFragment(Profile())
                R.id.settings -> replaceFragment(Settings())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
