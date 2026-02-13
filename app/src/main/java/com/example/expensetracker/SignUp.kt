package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expensetracker.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Signup text -> Login page
        binding.signup.setOnClickListener {
            val intent = (Intent(this, LoginPage::class.java))
            startActivity(intent)
            finish()
        }
        auth= FirebaseAuth.getInstance()
        binding.signupBtn.setOnClickListener{

            // Get text from user(edit text)
            val name = binding.userName.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            // Check if any field is blank
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ){
                Toast.makeText(this,"Please Fill All the Fields", Toast.LENGTH_SHORT).show()
            } else if (password.length<6){
                Toast.makeText(this,"Password Must be 6 Digits", Toast.LENGTH_LONG).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener (this){ task ->
                        if (task.isSuccessful){
                            Toast.makeText(this,"SignUp Successful", Toast.LENGTH_LONG).show()
                            val intent = (Intent(this, LoginPage::class.java))
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,"SignUp Failed : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}