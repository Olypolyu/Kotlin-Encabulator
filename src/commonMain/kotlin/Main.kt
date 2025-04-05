package org.example

import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.runBlocking
import org.example.data.BackendDataSource

val terminal = Terminal()

fun main() = runBlocking {
    // this is stupid because the data is evaluated at initialization instead of everytime it calls the jargon func...
    // i guess instead of having those functions...
    // i will fill in a separate field for errors and warnings that to do that eval just before print.
    terminal.info("aa")
    terminal.warn("aa")
    terminal.error("aa")

    BackendDataSource.processData(terminal)
}