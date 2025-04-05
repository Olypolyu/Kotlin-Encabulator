package org.example

import com.github.ajalt.mordant.terminal.Terminal
import org.example.data.BackendDataSource

val terminal = Terminal()

suspend fun main() {
    val source = BackendDataSource(terminal)

    source.initEnvironment()

    while (true) {
        val tasks = source.tasks.shuffled().iterator()
        while (tasks.hasNext()) tasks.next()()
    }
}
