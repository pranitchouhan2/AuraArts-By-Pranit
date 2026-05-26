package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artworks")
data class Artwork(
    @PrimaryKey val id: String,
    val title: String,
    val story: String,
    val category: String,
    val imageUrl: String,
    val materials: String,
    val creationTime: String,
    val price: Double,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)
