package com.example.eventsystem

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class EditEventActivity : Activity() {
    private lateinit var repository: EventRepository
    private var eventId: String? = null
    private var existingEvent: Event? = null

    private lateinit var typeSpinner: Spinner
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = EventRepository(this)
        eventId = intent.getStringExtra("event_id")
        existingEvent = eventId?.let { repository.getById(it) }

        val isEdit = existingEvent != null
        val root = screenRoot()
        root.addView(toolbar(if (isEdit) "Edit Event" else "Create Event", showBack = true))

        val form = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16.dp, 16.dp, 16.dp, 24.dp)
        }

        form.addLabel("Event type")
        typeSpinner = Spinner(this).apply {
            adapter = ArrayAdapter(
                this@EditEventActivity,
                android.R.layout.simple_spinner_dropdown_item,
                EventType.entries.map { it.label }
            )
            existingEvent?.let { setSelection(EventType.entries.indexOf(it.type)) }
            isEnabled = !isEdit
        }
        form.addView(typeSpinner, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        if (isEdit) {
            form.addView(TextView(this).apply {
                text = "Type is fixed after creation."
                textSize = 13f
                setPadding(0, 4.dp, 0, 8.dp)
            })
        }

        form.addLabel("Title")
        titleInput = EditText(this).apply {
            hint = "Enter title"
            setSingleLine(false)
            setText(existingEvent?.title.orEmpty())
        }
        form.addView(titleInput, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        form.addLabel("Description")
        descriptionInput = EditText(this).apply {
            hint = "Enter description"
            minLines = 4
            setText(existingEvent?.description.orEmpty())
        }
        form.addView(descriptionInput, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        form.addView(Button(this).apply {
            text = if (isEdit) "Save Changes" else "Create Event"
            setOnClickListener { saveEvent() }
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 20.dp, 0, 0) })

        root.addView(ScrollView(this).apply { addView(form) }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }

    private fun saveEvent() {
        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        if (title.isBlank()) {
            Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
            return
        }

        val current = existingEvent
        if (current == null) {
            val type = EventType.entries[typeSpinner.selectedItemPosition]
            repository.add(type, title, description)
        } else {
            repository.update(current.id, title, description)
        }
        finish()
    }

    private fun LinearLayout.addLabel(label: String) {
        addView(TextView(context).apply {
            text = label
            textSize = 14f
            setPadding(0, 12.dp, 0, 4.dp)
        })
    }
}
