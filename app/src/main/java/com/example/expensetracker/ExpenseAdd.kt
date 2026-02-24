package com.example.expensetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expensetracker.databinding.ActivityExpenseAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        val categories = arrayOf("Transport", "Food", "Shopping", "Health", "Entertainment")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        binding.autoCategory.setAdapter(adapter)

        binding.expenseAdd.setOnClickListener {
            val category = binding.autoCategory.text.toString()
            val amount = binding.Amount.text.toString()
            val note = binding.Note.text.toString()

            if (category.isEmpty())
                Toast.makeText(this,"Please select category", Toast.LENGTH_SHORT).show()

            else if (amount.isEmpty() || note.isEmpty())
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()

            val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val expense = Expense(category, amount, note, date)

            db.collection("Expenses")
                .add(expense)
                .addOnSuccessListener {
                    Toast.makeText(this,"Expense added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // back to home page
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this,"Failed to add expense: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}