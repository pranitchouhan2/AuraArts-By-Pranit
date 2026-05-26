package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "commission_inquiries")
data class CommissionInquiry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val artStyle: String,
    val size: String,
    val budget: String,
    val contactName: String = "",
    val status: String = "Inquiry Sent",
    val timestamp: Long = System.currentTimeMillis()
)
