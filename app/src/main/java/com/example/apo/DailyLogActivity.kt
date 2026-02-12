package com.example.apo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DailyLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_log)

        // 1. Get Date passed from previous screen
        val dateString = intent.getStringExtra("DATE_KEY") ?: "Today"
        findViewById<TextView>(R.id.tvDateHeader).text = dateString

        // 2. Setup Back Button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 3. Setup Recycler View with Data
        val rv = findViewById<RecyclerView>(R.id.rvDailyLogs)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = DailyLogAdapter(generateDummyLogs())
    }

    // Generate Dummy Data matching your screenshot
    private fun generateDummyLogs(): List<LogItem> {
        return listOf(
            LogItem("Sitting", "9:57 PM"),
            LogItem("Standing", "9:08 PM"),
            LogItem("Walking", "7:15 PM"),
            LogItem("Resting", "6:09 PM"),
            LogItem("Getting Up", "5:50 PM"),
            LogItem("Sitting", "4:27 PM"),
            LogItem("Resting", "3:51 PM"),
            LogItem("Walking", "2:11 PM"),
            LogItem("Getting Up", "1:18 PM")
        )
    }
}

// --- DATA MODEL ---
data class LogItem(val name: String, val time: String)

// --- ADAPTER ---
class DailyLogAdapter(private val items: List<LogItem>) : RecyclerView.Adapter<DailyLogAdapter.LogHolder>() {

    class LogHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvActivityName)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_log, parent, false)
        return LogHolder(view)
    }

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvTime.text = item.time
    }

    override fun getItemCount() = items.size
}