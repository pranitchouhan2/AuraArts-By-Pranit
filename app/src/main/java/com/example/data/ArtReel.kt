package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "art_reels")
data class ArtReel(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val thumbnailColor: String, // Hex styling or drawable
    val likesCount: Int = 142,
    val commentsCount: Int = 18,
    val isLiked: Boolean = false
)
