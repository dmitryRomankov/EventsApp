package com.example.eventsystem

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Activity.enableEdgeToEdge() {
    if (Build.VERSION.SDK_INT >= 30) {
        window.setDecorFitsSystemWindows(false)
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.WHITE
    if (Build.VERSION.SDK_INT >= 23) {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Activity.toolbar(
    title: String,
    showBack: Boolean = false,
    showMenu: Boolean = false,
    onMenuClick: (() -> Unit)? = null
): LinearLayout {
    val bar = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setBackgroundColor(Color.WHITE)
        setPadding(16.dp, statusBarPadding(), 16.dp, 12.dp)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        elevation = 6f
    }
    if (showMenu) {
        bar.addView(Button(this).apply {
            text = "☰"
            textSize = 20f
            contentDescription = "Open navigation menu"
            setOnClickListener { onMenuClick?.invoke() }
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }
    if (showBack) {
        bar.addView(Button(this).apply {
            text = "Back"
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }
    bar.addView(TextView(this).apply {
        text = title
        textSize = 20f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(Color.rgb(25, 25, 25))
        gravity = Gravity.CENTER_VERTICAL
        setPadding(if (showBack || showMenu) 12.dp else 0, 0, 0, 0)
    }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    return bar
}

fun Activity.navigationPanel(currentScreen: String): LinearLayout = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    setBackgroundColor(Color.WHITE)
    setPadding(18.dp, statusBarPadding() + 20.dp, 18.dp, 18.dp)
    visibility = View.GONE
    elevation = 16f
    translationX = -resources.displayMetrics.widthPixels / 2f
    layoutParams = FrameLayout.LayoutParams(
        resources.displayMetrics.widthPixels / 2,
        ViewGroup.LayoutParams.MATCH_PARENT,
        Gravity.START
    )

    addView(TextView(context).apply {
        text = "Navigation"
        textSize = 18f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(Color.rgb(40, 40, 40))
        setPadding(0, 0, 0, 16.dp)
    })

    addView(Button(context).apply {
        text = if (currentScreen == "Events") "Events ✓" else "Events"
        setOnClickListener {
            if (currentScreen != "Events") {
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        }
    }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

    addView(Button(context).apply {
        text = if (currentScreen == "User Profile") "User Profile ✓" else "User Profile"
        setOnClickListener {
            if (currentScreen != "User Profile") {
                context.startActivity(Intent(context, UserProfileActivity::class.java))
            }
        }
    }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
}

fun Activity.screenRoot(): LinearLayout = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    setBackgroundColor(Color.rgb(248, 248, 248))
    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
}

fun TextView.bodyText(value: String, bold: Boolean = false) {
    text = value
    textSize = 16f
    setTextColor(Color.rgb(45, 45, 45))
    if (bold) typeface = Typeface.DEFAULT_BOLD
}

fun formatDate(millis: Long): String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(millis))
fun formatDateOnly(millis: Long): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))

val Int.dp: Int get() = (this * android.content.res.Resources.getSystem().displayMetrics.density).toInt()

private fun Activity.statusBarPadding(): Int {
    val id = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (id > 0) resources.getDimensionPixelSize(id) else 24.dp
}
