package org.example

import com.github.ajalt.mordant.terminal.Terminal
import org.example.data.BackendDataSource
import java.text.SimpleDateFormat
import java.util.*

val terminal = Terminal()

val timestamp: String
    get() = SimpleDateFormat("hh:mm:ss.SSS").format(Date())

fun Terminal.info(string: String): String = "$timestamp [${theme.success("INFO")}] $string"
fun Terminal.warn(string: String): String = "$timestamp [${theme.warning("WARN")}] $string"
fun Terminal.err(string: String):  String = "$timestamp [${theme.danger("ERROR")}] $string"

suspend fun main() {
    // this is stupid because the data is evaluated at initialization instead of everytime it calls the jargon func...
    // i guess instead of having those functions...
    // i will fill in a separate field for errors and warnings that to do that eval just before print.
    terminal.println(terminal.info("aa"))
    terminal.println(terminal.warn("aa"))
    terminal.println(terminal.err("aa"))

    BackendDataSource.processData(terminal)
}