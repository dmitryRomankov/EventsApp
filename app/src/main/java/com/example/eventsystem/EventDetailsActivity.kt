package com.example.eventsystem

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class EventDetailsActivity : Activity() {
    private lateinit var repository: EventRepository
    private var eventId: String? = null
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = EventRepository(this)
        eventId = intent.getStringExtra("event_id")

        val root = screenRoot()
        root.addView(toolbar("Event Details", showBack = true))
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16.dp, 16.dp, 16.dp, 24.dp)
        }
        root.addView(ScrollView(this).apply { addView(content) }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        renderDetails()
    }

    private fun renderDetails() {
        content.removeAllViews()
        val event = eventId?.let { repository.getById(it) }
        if (event == null) {
            content.addView(TextView(this).apply { bodyText("Event was not found.") })
            return
        }

        content.addView(TextView(this).apply {
            text = event.title
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(25, 25, 25))
        })
        content.addInfo("Type", event.type.label)
        content.addInfo("Description", event.description.ifBlank { "No description" })
        content.addInfo("Created at", formatDate(event.createdAt))
        content.addInfo("Updated at", formatDate(event.updatedAt))
        content.addInfo("Created by", event.createdBy)

        content.addView(Button(this).apply {
            text = "Edit Event"
            setOnClickListener {
                startActivity(Intent(this@EventDetailsActivity, EditEventActivity::class.java).putExtra("event_id", event.id))
            }
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 20.dp, 0, 8.dp) })

        content.addView(Button(this).apply {
            text = "Delete Event"
            setTextColor(Color.rgb(130, 0, 0))
            setOnClickListener {
                repository.delete(event.id)
                finish()
            }
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    private fun LinearLayout.addInfo(label: String, value: String) {
        addView(TextView(context).apply {
            text = label
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(100, 100, 100))
            setPadding(0, 16.dp, 0, 2.dp)
        })
        addView(TextView(context).apply { bodyText(value) })
    }
}
