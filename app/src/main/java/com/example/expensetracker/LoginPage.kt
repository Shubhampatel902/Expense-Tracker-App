package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expensetracker.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    lateinit var auth: FirebaseAuth

    // Check if user already logIn
    override fun onRestart() {
        super.onRestart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        binding.loginbtn.setOnClickListener {
            val mail = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill all the Fields", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(mail,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomePage::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,"Login Failed :${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.signupTxt.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}