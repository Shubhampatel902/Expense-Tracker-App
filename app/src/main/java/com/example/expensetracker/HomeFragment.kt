package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseList: ArrayList<Expense>
    private lateinit var adapter: RecItemAdapter
    private val db = FirebaseFirestore.getInstance()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.greet.text = getGreeting()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenseList = ArrayList()
        adapter = RecItemAdapter(expenseList)
        recyclerView.adapter = adapter

        fetchExpenses()

        return binding.root
    }

    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 0..11 -> "Good Morning! 👋"
            in 12..15 -> "Good Afternoon! 👋"
            in 16..20 -> "Good Evening! 👋"
            else -> "Good Night! 👋"
        }
    }

    private fun fetchExpenses() {

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val currentCal = Calendar.getInstance()
        val currentMonth = currentCal.get(Calendar.MONTH)
        val currentYear = currentCal.get(Calendar.YEAR)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users")
            .document(userId)
            .collection("Expenses")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Failed to load expenses", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                expenseList.clear()

                var total = 0.0
                var monthly = 0.0
                var yearly = 0.0

                for (doc in value!!) {

                    val expense = doc.toObject(Expense::class.java)

                    // 🔥 IMPORTANT: document ID store karo
                    expense.id = doc.id

                    expenseList.add(expense)

                    val amount = expense.amount.toDoubleOrNull() ?: 0.0

                    if (expense.date.isBlank()) continue

                    val date = try {
                        sdf.parse(expense.date)
                    } catch (e: Exception) {
                        null
                    } ?: continue

                    val cal = Calendar.getInstance()
                    cal.time = date

                    val month = cal.get(Calendar.MONTH)
                    val year = cal.get(Calendar.YEAR)
                    total += amount

                    if (year == currentYear) {
                        yearly += amount
                    }

                    if (year == currentYear && month == currentMonth) {
                        monthly += amount
                    }
                }

                adapter.notifyDataSetChanged()

                // 🔥 Auto update UI
                binding.totalExpense.text = "₹ ${total.toInt()}"
                binding.monthlyExpense.text = "₹ ${monthly.toInt()}"
                binding.yearlyExpense.text = "₹ ${yearly.toInt()}"
            }
    }
}