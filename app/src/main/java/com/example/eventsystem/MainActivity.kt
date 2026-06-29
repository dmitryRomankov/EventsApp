package com.example.eventsystem

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var repository: EventRepository
    private lateinit var listContainer: LinearLayout
    private lateinit var navPanel: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        repository = EventRepository(this)

        val frameRoot = FrameLayout(this)
        val root = screenRoot()
        navPanel = navigationPanel("Events")
        root.addView(toolbar("Event System", showMenu = true) {
            toggleDrawer()
        })

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16.dp, 16.dp, 16.dp, 8.dp)
        }
        header.addView(TextView(this).apply {
            text = "Events"
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(30, 30, 30))
        }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        header.addView(Button(this).apply {
            text = "Add Event"
            setOnClickListener { startActivity(Intent(this@MainActivity, EditEventActivity::class.java)) }
        })
        root.addView(header)

        listContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12.dp, 0, 12.dp, 24.dp)
        }
        root.addView(ScrollView(this).apply { addView(listContainer) }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
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

    override fun onResume() {
        super.onResume()
        renderEvents()
    }

    private fun renderEvents() {
        listContainer.removeAllViews()
        val events = repository.getAll()
        if (events.isEmpty()) {
            listContainer.addView(TextView(this).apply {
                text = "No events yet. Tap Add Event to create one."
                textSize = 16f
                setPadding(16.dp, 24.dp, 16.dp, 24.dp)
            })
            return
        }
        events.forEach { event -> listContainer.addView(eventRow(event)) }
    }

    private fun eventRow(event: Event): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(Color.WHITE)
        setPadding(16.dp, 14.dp, 16.dp, 14.dp)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 8.dp, 0, 8.dp)
        layoutParams = params
        elevation = 2f
        isClickable = true
        setOnClickListener {
            startActivity(Intent(this@MainActivity, EventDetailsActivity::class.java).putExtra("event_id", event.id))
        }

        addView(TextView(context).apply { bodyText(event.title, bold = true) })
        addView(TextView(context).apply {
            bodyText("${event.type.label} • Updated ${formatDate(event.updatedAt)}")
            textSize = 14f
            setTextColor(Color.rgb(90, 90, 90))
            setPadding(0, 4.dp, 0, 0)
        })
    }
}
