package com.example.eventsystem

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class UserProfileActivity : Activity() {
    private lateinit var navPanel: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val frameRoot = FrameLayout(this)
        val root = screenRoot()
        navPanel = navigationPanel("User Profile")
        root.addView(toolbar("User Profile", showMenu = true) {
            toggleDrawer()
        })

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(20.dp, 20.dp, 20.dp, 20.dp)
            elevation = 2f
        }
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(16.dp, 16.dp, 16.dp, 0)
        card.layoutParams = params

        card.addView(TextView(this).apply {
            text = "Default User"
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(30, 30, 30))
            setPadding(0, 0, 0, 12.dp)
        })
        card.addView(profileLine("Name", "Default"))
        card.addView(profileLine("Surname", "User"))
        card.addView(profileLine("Date of registration", formatDateOnly(getRegistrationDate())))

        root.addView(card)
        frameRoot.addView(root)
        frameRoot.addView(navPanel)
        setContentView(frameRoot)
    }

    private fun toggleDrawer() {
        val drawerWidth = resources.displayMetrics.widthPixels / 2f
        if (navPanel.visibility == View.VISIBLE) {
            navPanel.animate()
                .translationX(-drawerWidth)
                .setDuration(180)
                .withEndAction { navPanel.visibility = View.GONE }
                .start()
        } else {
            navPanel.visibility = View.VISIBLE
            navPanel.translationX = -drawerWidth
            navPanel.animate()
                .translationX(0f)
                .setDuration(180)
                .start()
        }
    }

    private fun profileLine(label: String, value: String): TextView = TextView(this).apply {
        bodyText("$label: $value")
        setPadding(0, 8.dp, 0, 8.dp)
    }

    private fun getRegistrationDate(): Long {
        val prefs = getSharedPreferences("default_user", MODE_PRIVATE)
        val saved = prefs.getLong("registered_at", 0L)
        if (saved != 0L) return saved
        val now = System.currentTimeMillis()
        prefs.edit().putLong("registered_at", now).apply()
        return now
    }
}
