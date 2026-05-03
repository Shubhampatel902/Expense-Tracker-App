package com.example.expensetracker

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RecyclerItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class RecItemAdapter(
    private val expenseList: ArrayList<Expense>
) : RecyclerView.Adapter<RecItemAdapter.RecViewHolder>() {

    inner class RecViewHolder(val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val binding = RecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecViewHolder(binding)
    }

    data class CategoryUI(
        val imageRes: Int,
        val bgColor: Int
    )

    fun getCategoryUI(category: String): CategoryUI {
        return when (category) {
            "Food" -> CategoryUI(R.drawable.cutlery, Color.parseColor("#FFE0B2"))
            "Travel" -> CategoryUI(R.drawable.car, Color.parseColor("#BBDEFB"))
            "Shopping" -> CategoryUI(R.drawable.shopping, Color.parseColor("#E1BEE7"))
            "Health" -> CategoryUI(R.drawable.drugs, Color.parseColor("#C8E6C9"))
            else -> CategoryUI(R.drawable.ic_launcher_foreground, Color.LTGRAY)
        }
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        val expense = expenseList[position]

        val ui = getCategoryUI(expense.category)

        holder.binding.categoryImg.setImageResource(ui.imageRes)

        val drawable = holder.binding.frame.background as GradientDrawable
        drawable.setColor(ui.bgColor)

        holder.binding.tvTitle.text = expense.category
        holder.binding.tvAmount.text = "₹${expense.amount}"
        holder.binding.tvSubTitle.text = expense.note

        if (expense.timestamp != 0L) {

            val cal = java.util.Calendar.getInstance()
            cal.timeInMillis = expense.timestamp

            val today = java.util.Calendar.getInstance()
            val yesterday = java.util.Calendar.getInstance()
            yesterday.add(java.util.Calendar.DAY_OF_YEAR, -1)

            val displayText = when {
                isSameDay(cal, today) -> "Today"
                isSameDay(cal, yesterday) -> "Yesterday"
                else -> expense.date
            }

            holder.binding.tvDate.text = displayText

        } else {
            // old data fallback
            holder.binding.tvDate.text = expense.date
        }
        // 🔥 Long press delete (Firestore)
        holder.itemView.setOnLongClickListener {

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Delete") { _, _ ->

                    FirebaseFirestore.getInstance()
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid

                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("Expenses")
                        .document(expense.id)
                        .delete()

                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }
    }

    fun isSameDay(cal1: java.util.Calendar, cal2: java.util.Calendar): Boolean {
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }

    override fun getItemCount(): Int = expenseList.size
}