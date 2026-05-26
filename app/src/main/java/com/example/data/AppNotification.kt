package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_notifications")
data class AppNotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val text: String,
    val iconType: String, // "like", "order", "message", "offer", "trending"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
