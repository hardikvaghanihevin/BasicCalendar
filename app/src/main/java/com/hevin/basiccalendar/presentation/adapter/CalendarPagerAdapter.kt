package com.hevin.basiccalendar.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hevin.basiccalendar.presentation.ui.MonthFragment
import java.util.*

class CalendarPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val calendar = Calendar.getInstance()

    override fun getCount(): Int {
        return Int.MAX_VALUE // Allow infinite scroll (or fixed range based on your needs)
    }

    override fun getItem(position: Int): Fragment {
        // Calculate the month based on the current position and the calendar
        calendar.add(Calendar.MONTH, position - calendar.get(Calendar.MONTH))
        val fragment = MonthFragment()
        val bundle = Bundle()
        bundle.putInt("year", calendar.get(Calendar.YEAR))
        bundle.putInt("month", calendar.get(Calendar.MONTH))
        fragment.arguments = bundle
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        calendar.add(Calendar.MONTH, position - calendar.get(Calendar.MONTH))
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    }
}
