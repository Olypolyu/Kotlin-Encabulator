package com.github.olypolyu.ktencabulator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.runBlocking
import com.github.olypolyu.ktencabulator.data.BackendDataSource
import com.github.olypolyu.ktencabulator.data.DataSource

val environments: Array<String> = arrayOf(
    "backend"
)

class Application: CliktCommand() {
    private val projectName: String? by option("-n", "--name")
        .default("Project")
        .help { "Sets the project name during the Project Initialization phase." }

    private val environmentName: String? by option("-e", "--environment-name")
        .help { "Overrides the environment name during the Project Initialization phase." }

    private val environmentType: String by option("-t", "--environment-type")
        .choice(*environments)
        .default("backend")
        .help { "Sets the environment type. This affects what type of jargon is used." }

    private val jargonLevel: JargonLevel by option("-j", "--jargon-lv")
        .enum<JargonLevel>()
        .default(JargonLevel.MEDIUM)
        .help("Describes how hard the jargon will be to understand.")

    private val ansiLevel: AnsiLevel? by option("-a", "--ansii")
        .enum<AnsiLevel>()

    override fun run() = runBlocking {
        val sourceClass: Class<out DataSource> = when (environmentType) {
            "backend" -> BackendDataSource::class.java
            else -> throw UnknownError("\"$environmentType\" does not match a dataSource provider.")
        }

        val terminal = Terminal(
            ansiLevel = ansiLevel
        )

        terminal.cursor.move {
            setPosition(0,0)
            clearScreen()
        }

        val source = sourceClass
            .getConstructor(Terminal::class.java, JargonLevel::class.java, String::class.java, String::class.java)
            .newInstance(terminal, jargonLevel, projectName, environmentName)

        encabulate(source)
    }
}

suspend fun encabulate(source: DataSource) {
    source.initEnvironment()

    while (true) {
        val tasks = source.tasks.shuffled().iterator()
        while (tasks.hasNext()) tasks.next()()
    }
}

 fun main(args: Array<String>) = Application().main(args)
