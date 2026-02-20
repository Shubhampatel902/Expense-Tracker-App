package com.example.expensetracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expensetracker.databinding.ActivityExpenseAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseAdd : AppCompatActivity() {
    lateinit var binding: ActivityExpenseAddBinding
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExpenseAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseFirestore.getInstance()
        binding.expenseAdd.setOnClickListener {
            val category = binding.autoCategory.text.toString()
            val amount = binding.Amount.text.toString()
            val note = binding.Note.text.toString()

            if (category.isEmpty())
                Toast.makeText(this,"Please select category", Toast.LENGTH_SHORT).show()

            else if (amount.isEmpty() || note.isEmpty())
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()


        }
    }
}