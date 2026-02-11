package com.example.apo

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class AlertHistoryActivity : AppCompatActivity() {

    // Main calendar instance for tracking the currently displayed month
    private val displayCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_history)

        // 1. BACK BUTTON
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 2. SETUP DATE LIST (Recycler View)
        val rv = findViewById<RecyclerView>(R.id.rvDateList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = DateListAdapter(generateDateList()) { dateModel ->
            openDailyLog(dateModel.fullDate)
        }

        // 3. SETUP CALENDAR POPUP
        findViewById<View>(R.id.btnCalendar).setOnClickListener {
            showCustomCalendarDialog()
        }
    }

    private fun showCustomCalendarDialog() {
        // Inflate the custom narrow layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_calendar, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMonthYear = dialogView.findViewById<TextView>(R.id.tvMonthYear)
        val btnPrev = dialogView.findViewById<ImageButton>(R.id.btnPrevMonth)
        val btnNext = dialogView.findViewById<ImageButton>(R.id.btnNextMonth)
        val rvGrid = dialogView.findViewById<RecyclerView>(R.id.rvCalendarDays)

        rvGrid.layoutManager = GridLayoutManager(this, 7)

        // Function to refresh the calendar grid
        fun updateCalendar() {
            // Update Title (e.g., FEBRUARY 2026)
            val titleFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            tvMonthYear.text = titleFormat.format(displayCalendar.time).uppercase()

            val days = getDaysInMonth(displayCalendar)

            // Pass Year and Month to adapter to check for future dates
            rvGrid.adapter = CalendarGridAdapter(
                days,
                displayCalendar.get(Calendar.YEAR),
                displayCalendar.get(Calendar.MONTH)
            ) { day ->
                if (day.isNotEmpty()) {
                    // Handle Click on Valid Date
                    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    val monthYear = monthFormat.format(displayCalendar.time)
                    val fullDate = "$day $monthYear"
                    dialog.dismiss()
                    openDailyLog(fullDate)
                }
            }
        }

        // Initial Load
        updateCalendar()

        // Handle Month Navigation
        btnPrev.setOnClickListener {
            displayCalendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }
        btnNext.setOnClickListener {
            displayCalendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        dialog.show()
    }

    // Helper to generate the list of numbers for the grid
    private fun getDaysInMonth(currentCal: Calendar): List<String> {
        val days = mutableListOf<String>()
        val tempCal = currentCal.clone() as Calendar
        tempCal.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) // 1=Sun, 2=Mon...
        val maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Add empty slots for days before the 1st
        for (i in 1 until firstDayOfWeek) {
            days.add("")
        }

        // Add days 1..31
        for (i in 1..maxDays) {
            days.add(i.toString())
        }
        return days
    }

    private fun openDailyLog(date: String) {
        val intent = Intent(this, DailyLogActivity::class.java)
        intent.putExtra("DATE_KEY", date)
        startActivity(intent)
    }

    private fun generateDateList(): List<DateModel> {
        val list = mutableListOf<DateModel>()
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        for (i in 0..14) {
            val dateStr = dateFormat.format(cal.time)
            val dayStr = dayFormat.format(cal.time)
            list.add(DateModel(dateStr, dayStr, "$dateStr $dayStr"))
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        return list
    }
}

// ==========================================
// DATA MODELS & ADAPTERS
// ==========================================

data class DateModel(val datePart: String, val dayPart: String, val fullDate: String)

// 1. Adapter for the Main Date List (Rows)
class DateListAdapter(
    private val dates: List<DateModel>,
    private val onClick: (DateModel) -> Unit
) : RecyclerView.Adapter<DateListAdapter.DateViewHolder>() {

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDay: TextView = view.findViewById(R.id.tvDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_row, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val item = dates[position]
        holder.tvDate.text = item.datePart
        holder.tvDay.text = item.dayPart
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = dates.size
}

// 2. Adapter for the Calendar Grid (Popup)
class CalendarGridAdapter(
    private val days: List<String>,
    private val displayYear: Int,
    private val displayMonth: Int,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarGridAdapter.DayViewHolder>() {

    private val todayCal = Calendar.getInstance()

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayStr = days[position]
        holder.tvDay.text = dayStr

        if (dayStr.isEmpty()) {
            holder.itemView.isClickable = false
            return
        }

        // Logic to check if date is in future
        val day = dayStr.toInt()
        val cellCal = Calendar.getInstance()
        cellCal.set(displayYear, displayMonth, day)

        // Reset time to midnight for accurate comparison
        resetTime(cellCal)
        resetTime(todayCal)

        if (cellCal.after(todayCal)) {
            // FUTURE DATE: Grey out and disable click
            holder.tvDay.setTextColor(Color.LTGRAY)
            holder.itemView.isClickable = false
            holder.itemView.setOnClickListener(null)
        } else {
            // PAST/PRESENT: Black text and clickable
            holder.tvDay.setTextColor(Color.BLACK)
            holder.itemView.isClickable = true
            holder.itemView.setOnClickListener { onClick(dayStr) }
        }
    }

    private fun resetTime(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }

    override fun getItemCount() = days.size
}