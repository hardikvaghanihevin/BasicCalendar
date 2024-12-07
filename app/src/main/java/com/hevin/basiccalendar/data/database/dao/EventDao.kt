package com.hevin.basiccalendar.data.database.dao

import androidx.room.*
import com.hevin.basiccalendar.domain.model.Event

@Dao
interface EventDao {

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE isHoliday = 1")
    suspend fun getHolidayEvents(): List<Event>
}
