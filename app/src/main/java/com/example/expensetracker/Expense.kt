package com.example.expensetracker

data class Expense(
    var id : String = "",
    val category : String = "",
    val amount : String = "",
    val note : String = "",
    val date : String = "",
    val timestamp: Long = 0L
)
