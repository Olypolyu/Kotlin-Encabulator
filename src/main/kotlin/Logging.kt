package org.example

import com.github.ajalt.mordant.terminal.Terminal
import java.text.SimpleDateFormat
import java.util.Date

private val timestamp: String
    get() = SimpleDateFormat("hh:mm:ss.SSS").format(Date())

private enum class LogLevel {
    INFO, WARN, ERROR;
}

private fun Terminal.log(level: LogLevel, message: String) {
    val style = when (level) {
        LogLevel.INFO -> theme.success("INFO")
        LogLevel.WARN -> theme.warning("WARN")
        LogLevel.ERROR -> theme.danger("ERROR")
    }

    this.println("$timestamp [$style] $message")
}

fun Terminal.info(message: String) = log(LogLevel.INFO, message)
fun Terminal.warn(message: String) = log(LogLevel.WARN, message)
fun Terminal.error(message: String) = log(LogLevel.ERROR, message)