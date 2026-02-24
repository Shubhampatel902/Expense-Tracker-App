package com.example.expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RecyclerItemBinding

class RecItemAdapter(
    private val expenseList: ArrayList<Expense>
) : RecyclerView.Adapter<RecItemAdapter.RecViewHolder>() {

    inner class RecViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        val expense = expenseList[position]

        // Example: Assuming your item_expense.xml has these TextViews
        holder.binding.tvTitle.text = expense.category
        holder.binding.tvAmount.text = "₹${expense.amount}"
        holder.binding.tvSubTitle.text = expense.note
        holder.binding.tvDate.text = expense.date
    }

    override fun getItemCount(): Int = expenseList.size
}