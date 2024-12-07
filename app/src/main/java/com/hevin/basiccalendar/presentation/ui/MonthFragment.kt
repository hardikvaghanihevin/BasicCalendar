package com.hevin.basiccalendar.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hevin.basiccalendar.R
import java.util.*

class MonthFragment : Fragment(R.layout.fragment_month) {

    private lateinit var gridDays: GridLayout
    private lateinit var tvMonthYear: TextView
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the passed arguments (year and month)
        val year = arguments?.getInt("year") ?: Calendar.getInstance().get(Calendar.YEAR)
        val month = arguments?.getInt("month") ?: Calendar.getInstance().get(Calendar.MONTH)

        // Set the calendar to the selected month and year
        calendar.set(year, month, 1)

        gridDays = view.findViewById(R.id.gridDays)
        tvMonthYear = view.findViewById(R.id.tvMonthYear)

        // Set the month and year display
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())!!
        tvMonthYear.text = "$monthName $year"

        // Populate the grid with the days of the month
        populateCalendarDays()
    }

    private fun populateCalendarDays() {
        gridDays.removeAllViews()

        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, etc.
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Add empty views for the days before the 1st of the month
        for (i in 1 until firstDayOfMonth) {
            val emptyView = View(context)
            emptyView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                setMargins(8, 8, 8, 8)
            }
            gridDays.addView(emptyView)
        }

        // Add the actual days to the grid
        for (day in 1..daysInMonth) {
            val dayTextView = TextView(context)
            dayTextView.text = day.toString()
            dayTextView.textSize = 18f
            dayTextView.setPadding(16, 16, 16, 16)
            dayTextView.setBackgroundResource(android.R.drawable.btn_default)
            dayTextView.setOnClickListener {
                Toast.makeText(context, "Selected day: $day", Toast.LENGTH_SHORT).show()
            }

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec((firstDayOfMonth + day - 2) % 7) // Positioning the day correctly
                rowSpec = GridLayout.spec((firstDayOfMonth + day - 2) / 7)
            }

            dayTextView.layoutParams = params
            gridDays.addView(dayTextView)
        }
    }
}
