package com.example.apo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.apo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ==========================================
        // 1. SIDE MENU LOGIC (Open & Close)
        // ==========================================

        // OPEN Drawer (Hamburger Button)
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // CLOSE Drawer (The 'X' Button inside the Header)
        // We must get the Header View first because it's inside the NavigationView
        val headerView = binding.navView.getHeaderView(0)
        val btnCloseDrawer = headerView.findViewById<ImageButton>(R.id.btnCloseDrawer)

        btnCloseDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // ==========================================
        // 2. DASHBOARD BUTTONS
        // ==========================================

        // LOGOUT BUTTON
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show()
            // Example: Navigate back to Login Screen
            // val intent = Intent(this, LoginActivity::class.java)
            // startActivity(intent)
            // finish()
        }

        // SNAPSHOT BUTTON
        binding.btnSnapshot.setOnClickListener {
            Toast.makeText(this, "Snapshot Saved!", Toast.LENGTH_SHORT).show()
        }

        // RECORD BUTTON
        binding.btnRecord.setOnClickListener {
            Toast.makeText(this, "Recording Started...", Toast.LENGTH_SHORT).show()
        }

        // ==========================================
        // 3. MENU NAVIGATION ITEMS
        // ==========================================
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_alerts -> {
                    // Navigate to Alert History
                    val intent = Intent(this, AlertHistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_playback -> {
                    // Navigate to Playback (Placeholder)
                    val intent = Intent(this, PlaybackSelectionActivity::class.java)
                    startActivity(intent)                }
            }
            // Close drawer after clicking an item
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Handle Back Press to close Drawer first
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}