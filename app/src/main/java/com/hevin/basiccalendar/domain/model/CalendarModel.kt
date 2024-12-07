package com.hevin.basiccalendar.domain.model

data class CalendarModel (
    val day:Int,
    val date:String,
    val state:Int,
    var isSelected: Boolean = false,
    var eventIndicator: Boolean = false,
    var event: Event = Event(title = "", startTime = 0L, endTime = 0L)
)