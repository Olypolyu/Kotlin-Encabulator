package org.example.data

import com.github.ajalt.mordant.animation.coroutines.animateInCoroutine
import com.github.ajalt.mordant.animation.progress.advance
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.progress.*
import kotlinx.coroutines.*
import org.example.JargonLevel
import kotlin.math.min

// TODO: Put better names, just added this to make code a bit clearer
data class Process(val text: String, val suffix: String?)

abstract class DataSource {
    abstract val networkEndpoints: List<String>?
    protected abstract val process: List<Process>

    abstract val softJargon: List<String>
    abstract val mediumJargon: List<String>
    abstract val hardJargon: List<String>
    abstract val boiledJargon: List<String>

    fun jargon(level: JargonLevel): String = when (level) {
        JargonLevel.SOFT -> softJargon
        JargonLevel.MEDIUM -> mediumJargon
        JargonLevel.HARD -> hardJargon
        JargonLevel.BOILED -> boiledJargon
    }.random()

    suspend fun processData(terminal: Terminal) = coroutineScope {
        val (text, suffix) = process.random()

        val progress = progressBarLayout {
            marquee(terminal.theme.warning(text), width = min(terminal.size.width/2, text.length))

            percentage()
            progressBar()
            if (suffix != null) speed(suffix, style = terminal.theme.info)
        }.animateInCoroutine(terminal)

        launch { progress.execute() }
        progress.update { total = 100 }

        while (!progress.finished) {
            progress.advance(1)

            delay((100..300).random().toLong())
        }
    }
}