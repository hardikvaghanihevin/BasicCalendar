package com.hevin.basiccalendar.presentation.ui

import android.Manifest
import android.Manifest.permission.WRITE_CALENDAR
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.hevin.basiccalendar.R
import com.hevin.basiccalendar.common.Constants
import com.hevin.basiccalendar.databinding.FragmentCalendarBinding
import com.hevin.basiccalendar.domain.model.CalendarModel
import com.hevin.basiccalendar.domain.model.Event
import com.hevin.basiccalendar.domain.repository.DateItemClickListener
import com.hevin.basiccalendar.presentation.adapter.CalendarAdapter
import com.hevin.basiccalendar.presentation.adapter.EventAdapter
import com.hevin.basiccalendar.utillities.createDate
import com.hevin.basiccalendar.utillities.getMonth
import com.hevin.basiccalendar.utillities.isItToday
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class CalendarFragment : Fragment(R.layout.fragment_calendar), DateItemClickListener {
    private final val TAG = Constants.BASE_TAG + CalendarFragment::class.java.simpleName


    private var currDate = DateTime()
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter

    // ViewBinding setup
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    // Fragment arguments (if needed in future)
    private var param1: String? = null

    private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentCalendarBinding.bind(view)

        binding.apply {
            includedMonthNavigation.apply {
                rvCalender.layoutManager = GridLayoutManager(requireContext(), 7)
                rvCalender.addItemDecoration(object : ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.top = 0
                        outRect.bottom = 0
                    }
                })
                rvCalender.setHasFixedSize(true)

                rvEvent.layoutManager = LinearLayoutManager(requireContext())
                rvEvent.addItemDecoration(object : ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.top = 0
                        outRect.bottom = 0
                    }
                })
                rvEvent.setHasFixedSize(true)

                btnPrevMonth.setOnClickListener {
                    currDate = currDate.minusMonths(1)
                    setCalender(currDate)
                    tvMonthYear.text = getMonth(currDate.monthOfYear,requireContext()) +", " + currDate.year

                    setupCalendarView()
                }
                btnNextMonth.setOnClickListener {
                    currDate = currDate.plusMonths(1)
                    setCalender(currDate)
                    tvMonthYear.text = getMonth(currDate.monthOfYear,requireContext()) +", " + currDate.year

                    setupCalendarView()
                }
            }

        }
        // Check calendar permissions and load events if granted
        checkCalendarPermissions()

        setCalender(currDate)
        // Setup the calendar date change listener
        setupCalendarView()

    }

    private var date: DateTime? = null
    private var dateList: MutableList<CalendarModel>? = null
    @SuppressLint("SetTextI18n")
    private fun setCalender(currDate: DateTime) {
        binding.includedMonthNavigation.tvMonthYear.text = getMonth(currDate.monthOfYear,requireContext()) +", " + currDate.year
        date = currDate.withTime(0,0,0,0)
        val numOfDayInThisMonth = date!!.dayOfMonth().maximumValue
        date = date!!.minusDays(date!!.dayOfMonth().get() )//date!!.minusDays(date!!.dayOfMonth().get() -1)
        var dayOfWeek = date!!.dayOfWeek

        if (dayOfWeek == 7) {
            dayOfWeek = 0
        }

        if (dateList == null) {
            dateList = ArrayList()
        }else{
            dateList!!.clear()
        }

        for (i in 1..(numOfDayInThisMonth + dayOfWeek)){
            var model : CalendarModel = if (i <= dayOfWeek){
                CalendarModel(0,"",-1)
            }else{
                val dateTemp = createDate(i - dayOfWeek,currDate.monthOfYear,currDate.year)

                val dateKey = dateTemp?.toLocalDate().toString()
                val event = eventMap[dateKey] ?: Event(title = "", startTime = 0L, endTime = 0L)

                when(isItToday(dateTemp!!))
                {
                    0 -> {
                        // past days
                        CalendarModel(i - dayOfWeek,dateTemp.toLocalDate().toString(), 3, event = event ) }
                    1 -> {
                        // today
                        CalendarModel(day = i - dayOfWeek, date = dateTemp.toLocalDate().toString(), state = 2, isSelected = true, event = event)}
                    else -> {
                        // future days
                        CalendarModel(i - dayOfWeek,dateTemp.toLocalDate().toString(),1, event = event)}
                }
            }
            dateList!!.add(model)
            Log.d(TAG, "setCalender: ${dateList!!.size}")

        }
        calendarAdapter = CalendarAdapter(dateList as ArrayList<CalendarModel>, this)
        binding.includedMonthNavigation.rvCalender.adapter = calendarAdapter

        //region Filter events that match dates in dateList and create a new list
        //endregion
        // TODO: Add filtering logic to show only events that match the current month and year in the dateList
        // Use the filteredEvents list to populate the EventAdapter
        val filteredEvents: List<Event> = dateList!!.mapNotNull { calendarModel ->
            val eventDate = calendarModel.date // Get date string from CalendarModel
            eventMap[eventDate] // If event exists in eventMap for this date, add to list; else null
        }

        eventAdapter = EventAdapter(filteredEvents as ArrayList<Event>, this)
        binding.includedMonthNavigation.rvEvent.adapter = eventAdapter


    }

    override fun onDateClick(position: Int, calendarModel: CalendarModel) {
//        Toast.makeText(requireContext(), "Date: ${calendarModel.date}", Toast.LENGTH_SHORT).show()
        setupCalendarView()
        calendarAdapter.updateItem(position = position, calendarModel)
    }

    // Set up CalendarView's date change listener
    private fun setupCalendarView() {
        Log.d(TAG, "setupCalendarView: ")

        currentYear = currDate.year
        currentMonth = currDate.monthOfYear - 1

        // Load events and then set the calendar view
        loadCalendarEvents(currentYear, currentMonth)
        setCalender(currDate)
    }

    // Method to load events for the specific month and year
     private val eventMap = mutableMapOf<String, Event>()

    @SuppressLint("Range")
    private fun loadCalendarEvents(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        val startOfMonth = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val endOfMonth = calendar.timeInMillis

        val selection = "(${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?)"
        val selectionArgs = arrayOf(startOfMonth.toString(), endOfMonth.toString())
        val eventsUri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DESCRIPTION
        )

        val cursor = requireContext().contentResolver.query(
            eventsUri, projection, selection, selectionArgs,
            CalendarContract.Events.DTSTART + " ASC"
        )

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val title = it.getString(it.getColumnIndex(CalendarContract.Events.TITLE))
                    val start = it.getLong(it.getColumnIndex(CalendarContract.Events.DTSTART))
                    val end = it.getLong(it.getColumnIndex(CalendarContract.Events.DTEND))
                    val description = it.getString(it.getColumnIndex(CalendarContract.Events.DESCRIPTION)) ?: ""

                    val formattedStart = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(start))
                    val formattedEnd = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(end))

                    val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(start))
                    val event = Event(title = title, startTime = start, endTime = end, description = description)
                    eventMap[dateKey] = event

                } while (it.moveToNext())
            }
            it.close()
        }
    }

    // Check for calendar read permissions and request if necessary
    private fun checkCalendarPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents(currentYear,currentMonth)
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CALENDAR, WRITE_CALENDAR),
                REQUEST_CODE_READ_CALENDAR
            )
        }
    }

    // Handle permission result (if user grants or denies permission)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_READ_CALENDAR && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, load events
//            loadCalendarEvents()
            loadCalendarEvents(currentYear, currentMonth)
        } else {
            // Permission denied, notify the user
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Clean up the ViewBinding reference when the view is destroyed to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify binding reference
    }

    companion object {
        // Request code for calendar permission
        private const val REQUEST_CODE_READ_CALENDAR = 1
    }
}
