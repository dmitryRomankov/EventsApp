# EventSystemApp

Minimal Android Studio project written in Kotlin.

## Features

- Event list screen with edge-to-edge layout and toolbar.
- Event details screen with toolbar and back button.
- Create/edit screen with toolbar and back button.
- Two fixed event types: Conference and Meetup.
- Event fields: Title, Description, CreatedAt, UpdatedAt, CreatedBy(username).
- One default user: `default_user`.
- CRUD: create, read, update, delete events.
- Persistent storage using SharedPreferences with JSON, so data remains after the app is closed from task manager and reopened.
- Orientation changes supported by responsive programmatic layouts and `fullSensor` orientation.

## How to run

1. Open this folder in Android Studio.
2. Let Android Studio sync Gradle and download required SDK/plugin files. The project uses Groovy Gradle scripts to avoid Kotlin DSL source-resolution issues in Android Studio.
3. Select an emulator or connected Android device.
4. Click Run.

## Project structure

- `MainActivity.kt` — event list and Add Event button.
- `EventDetailsActivity.kt` — selected event details, edit, delete.
- `EditEventActivity.kt` — create and edit event form.
- `EventRepository.kt` — persistent storage.
- `Event.kt` — model and event type definitions.


## Build note

This version avoids a forced Java toolchain. Java and Kotlin targets are both set to 1.8, so Android Studio can use its bundled JDK instead of requiring a separate JDK 17 installation.

## Navigation update
- The Events screen now has a hamburger button in the top-left toolbar.
- Tapping it opens a simple navigation panel with Events and User Profile items.
- User Profile shows the default user's name, surname, and saved registration date.


Update: The hamburger button now opens a left-side drawer that overlays approximately half of the main screen.
