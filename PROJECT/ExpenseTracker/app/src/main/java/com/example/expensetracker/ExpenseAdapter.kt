package com.example.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private var expenses: List<ExpenseEntity>,
    private val onClick: (ExpenseEntity) -> Unit,
    private val onLongClick: (ExpenseEntity) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvNote: TextView = view.findViewById(R.id.tvDescription)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val ivCategoryIcon: ImageView = view.findViewById(R.id.ivCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.tvCategory.text = expense.category
        
        val sign = if (expense.type == "Income") "+" else "-"
        holder.tvAmount.text = String.format(Locale.getDefault(), "%s ₹ %.2f", sign, expense.amount)
        
        val color = if (expense.type == "Income") {
            android.R.color.holo_green_dark
        } else {
            android.R.color.holo_red_dark
        }
        holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, color))
        
        holder.tvNote.text = expense.note
        
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvDate.text = sdf.format(Date(expense.date))

        // Set Icon based on category
        val iconRes = when (expense.category) {
            "Food" -> android.R.drawable.ic_menu_today
            "Travel" -> android.R.drawable.ic_dialog_map
            "Shopping" -> android.R.drawable.ic_menu_view
            "Bills" -> android.R.drawable.ic_menu_send
            "Entertainment" -> android.R.drawable.ic_menu_slideshow
            "Income" -> android.R.drawable.ic_input_add
            else -> android.R.drawable.ic_menu_agenda
        }
        holder.ivCategoryIcon.setImageResource(iconRes)

        holder.itemView.setOnClickListener {
            onClick(expense)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(expense)
            true
        }
    }

    override fun getItemCount() = expenses.size

    fun updateData(newExpenses: List<ExpenseEntity>) {
        val diffCallback = ExpenseDiffCallback(expenses, newExpenses)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        expenses = newExpenses
        diffResult.dispatchUpdatesTo(this)
    }

    fun getExpenseAt(position: Int): ExpenseEntity {
        return expenses[position]
    }

    class ExpenseDiffCallback(
        private val oldList: List<ExpenseEntity>,
        private val newList: List<ExpenseEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
