package com.elijahneville.vegez.data

data class Receipt(
    val totalCost: Double,
    val amountGiven: Double,
    val changeDue: Double,
    val cashierName: String,
    val items: List<String>
)
