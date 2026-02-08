package com.example.apo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DailyLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_log)

        // 1. Get Date from Intent (passed from previous screen)
        val date = intent.getStringExtra("DATE_KEY") ?: "Today"
        findViewById<TextView>(R.id.tvDateTitle).text = date

        // 2. Back Button
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        // 3. Setup Table Data (Mock Data based on wireframe)
        val logs = listOf(
            LogItem("Standing", "9:00 A.M."),
            LogItem("Resting", "8:00 A.M."),
            LogItem("Standing", "7:00 A.M."),
            LogItem("Getting Up", "6:00 A.M."),
            LogItem("Resting", "5:00 A.M."),
            LogItem("Getting Up", "4:00 A.M."),
            LogItem("Resting", "3:00 A.M.")
        )

        val rv = findViewById<RecyclerView>(R.id.rvDailyLogs)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = DailyLogAdapter(logs)
    }
}

// Data Class
data class LogItem(val activity: String, val time: String)

// Adapter
class DailyLogAdapter(private val logs: List<LogItem>) : RecyclerView.Adapter<DailyLogAdapter.LogViewHolder>() {

    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvActivity: TextView = view.findViewById(android.R.id.text1)
        val tvTime: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        // Using standard Android layout "simple_list_item_2" which has two text views
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = logs[position]

        // Activity Column
        holder.tvActivity.text = item.activity
        holder.tvActivity.textSize = 16f

        // Time Column (Aligning it to look like a table)
        holder.tvTime.text = item.time
        holder.tvTime.textSize = 16f
        holder.tvTime.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
    }

    override fun getItemCount() = logs.size
}