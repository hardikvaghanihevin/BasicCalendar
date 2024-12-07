package com.hevin.basiccalendar.domain.repository

import com.hevin.basiccalendar.domain.model.CalendarModel

interface DateItemClickListener {
    fun onDateClick(position: Int,calendarModel: CalendarModel)
}