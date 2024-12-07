package com.hevin.basiccalendar.domain.model

import android.content.ClipDescription
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val startTime: Long, // Timestamp
    val endTime: Long,   // Timestamp
    val isHoliday: Boolean = false, // To differentiate holiday events
    val description: String = ""
)
