package com.example.apo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedSnapshotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_snapshots)

        // 1. Back Button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 2. Setup Grid RecyclerView
        val rv = findViewById<RecyclerView>(R.id.rvSnapshots)
        rv.layoutManager = GridLayoutManager(this, 3) // 3 Columns wide
        rv.adapter = SnapshotAdapter(generateDummyData())
    }

    // Fake Data Generator
    private fun generateDummyData(): List<SnapshotModel> {
        val list = mutableListOf<SnapshotModel>()
        for (i in 1..15) {
            list.add(SnapshotModel("Feb 8", "10:${i}0 AM"))
        }
        return list
    }
}

// --- DATA MODEL ---
data class SnapshotModel(val date: String, val time: String)

// --- ADAPTER ---
class SnapshotAdapter(private val items: List<SnapshotModel>) : RecyclerView.Adapter<SnapshotAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_snapshot, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.tvDate.text = "${item.date} • ${item.time}"
    }

    override fun getItemCount() = items.size
}