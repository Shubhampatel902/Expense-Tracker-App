package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseList: ArrayList<Expense>
    private lateinit var adapter: RecItemAdapter
    private val db = FirebaseFirestore.getInstance()  // Firestore instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenseList = ArrayList()
        adapter = RecItemAdapter(expenseList)
        recyclerView.adapter = adapter

        // Fetch saved expenses from Firestore
        fetchExpenses()

        return view
    }

    private fun fetchExpenses() {
        db.collection("Expenses")
            .orderBy("date")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Failed to load expenses", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                expenseList.clear()
                for (doc in value!!) {
                    val expense = doc.toObject(Expense::class.java)
                    expenseList.add(expense)
                }

                // Notify adapter to refresh RecyclerView
                adapter.notifyDataSetChanged()
            }
    }
}