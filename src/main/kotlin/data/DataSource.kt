package org.example.data

import com.github.ajalt.mordant.animation.coroutines.animateInCoroutine
import com.github.ajalt.mordant.animation.progress.advance
import com.github.ajalt.mordant.animation.progress.update
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.progress.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.example.JargonLevel
import kotlin.math.min
import kotlin.random.Random

abstract class DataSource {
    abstract val networkEndpoints: List<String>?
    protected abstract val process: List<Pair<String, String?>>

    abstract val softJargon: List<String>
    abstract val mediumJargon: List<String>
    abstract val hardJargon: List<String>
    abstract val boiledJargon: List<String>

    fun getJargon(level: JargonLevel): String {
        return when (level) {
            JargonLevel.SOFT -> softJargon.random()
            JargonLevel.MEDIUM -> mediumJargon.random()
            JargonLevel.HARD -> hardJargon.random()
            JargonLevel.BOILED -> boiledJargon.random()
        }
    }

    suspend fun processData(terminal: Terminal) = coroutineScope {
        val str = process.random()

        val progress = progressBarLayout {
            marquee(terminal.theme.warning(str.first), width = min(terminal.size.width/2, str.first.length))

            percentage()
            progressBar()
            if (str.second != null) speed(str.second!!, style = terminal.theme.info)
        }   .animateInCoroutine(terminal)

        progress.update { total = 100 }
        launch { progress.execute() }

        while (!progress.finished) {
            progress.advance(1)

            Thread.sleep(Random.nextLong(100, 300))
        }
    }
}