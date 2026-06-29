package com.example.eventsystem

import android.content.Context
import org.json.JSONArray
import java.util.UUID

class EventRepository(context: Context) {
    private val prefs = context.getSharedPreferences("event_storage", Context.MODE_PRIVATE)

    fun getAll(): List<Event> {
        seedIfEmpty()
        val json = prefs.getString(KEY_EVENTS, "[]") ?: "[]"
        val array = JSONArray(json)
        return (0 until array.length())
            .map { Event.fromJson(array.getJSONObject(it)) }
            .sortedByDescending { it.updatedAt }
    }

    fun getById(id: String): Event? = getAll().firstOrNull { it.id == id }

    fun add(type: EventType, title: String, description: String): Event {
        val now = System.currentTimeMillis()
        val event = Event(
            id = UUID.randomUUID().toString(),
            type = type,
            title = title.trim(),
            description = description.trim(),
            createdAt = now,
            updatedAt = now
        )
        saveAll(getAll() + event)
        return event
    }

    fun update(id: String, title: String, description: String): Event? {
        var updated: Event? = null
        val newList = getAll().map { current ->
            if (current.id == id) {
                current.copy(
                    title = title.trim(),
                    description = description.trim(),
                    updatedAt = System.currentTimeMillis()
                ).also { updated = it }
            } else current
        }
        saveAll(newList)
        return updated
    }

    fun delete(id: String) {
        saveAll(getAll().filterNot { it.id == id })
    }

    private fun saveAll(events: List<Event>) {
        val array = JSONArray()
        events.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY_EVENTS, array.toString()).apply()
    }

    private fun seedIfEmpty() {
        if (prefs.contains(KEY_EVENTS)) return
        val now = System.currentTimeMillis()
        val sampleEvents = listOf(
            Event(UUID.randomUUID().toString(), EventType.CONFERENCE, "Android Study Conference", "A simple conference for Kotlin learners.", now - 86_400_000, now - 86_400_000),
            Event(UUID.randomUUID().toString(), EventType.MEETUP, "Local Developer Meetup", "Meet other developers and discuss app ideas.", now - 43_200_000, now - 43_200_000)
        )
        saveAll(sampleEvents)
    }

    companion object {
        private const val KEY_EVENTS = "events"
    }
}
