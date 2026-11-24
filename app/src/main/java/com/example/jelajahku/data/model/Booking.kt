package com.example.jelajahku.data.model

data class Booking(
    val id: Int,
    val destination: String,
    val date: String,
    val status: String,
    val totalPrice: Int = 0,
    val bookedAt: String? = null
)

