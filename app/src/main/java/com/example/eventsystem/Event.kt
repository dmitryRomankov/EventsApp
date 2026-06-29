package com.example.eventsystem

import org.json.JSONObject

enum class EventType(val label: String) {
    CONFERENCE("Conference"),
    MEETUP("Meetup");

    companion object {
        fun fromName(name: String): EventType = entries.firstOrNull { it.name == name } ?: CONFERENCE
    }
}

data class Event(
    val id: String,
    val type: EventType,
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
    val createdBy: String = DEFAULT_USER
) {
    fun toJson(): JSONObject = JSONObject()
        .put("id", id)
        .put("type", type.name)
        .put("title", title)
        .put("description", description)
        .put("createdAt", createdAt)
        .put("updatedAt", updatedAt)
        .put("createdBy", createdBy)

    companion object {
        const val DEFAULT_USER = "default_user"

        fun fromJson(json: JSONObject): Event = Event(
            id = json.getString("id"),
            type = EventType.fromName(json.getString("type")),
            title = json.getString("title"),
            description = json.getString("description"),
            createdAt = json.getLong("createdAt"),
            updatedAt = json.getLong("updatedAt"),
            createdBy = json.optString("createdBy", DEFAULT_USER)
        )
    }
}
