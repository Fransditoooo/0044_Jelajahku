package com.example.jelajahku.data.model

data class Destination(
    val id: Int,
    val name: String,
    val city: String,
    val category: String,
    val description: String,
    val price: Int,
    val imageUrl: String? = null
)

