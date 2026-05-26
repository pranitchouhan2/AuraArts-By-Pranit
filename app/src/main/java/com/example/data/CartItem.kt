package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int = 1
)
