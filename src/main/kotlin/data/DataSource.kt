package org.example.data

import com.github.ajalt.mordant.animation.coroutines.animateInCoroutine
import com.github.ajalt.mordant.animation.progress.advance
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.Spinner
import com.github.ajalt.mordant.widgets.progress.*
import kotlinx.coroutines.*
import org.example.info
import org.example.warn
import kotlin.math.min

// TODO: Put better names, just added this to make code a bit clearer
data class Process(val text: String, val suffix: String?)

data class Analysis(val name: String, val range: IntRange, val suffix:String = "", val labelFn: (Int) -> Pair<String, Boolean>)

abstract class DataSource(private val terminal: Terminal) {
    abstract val fileExtension: List<String>

    val tasks: List<suspend () -> Unit> = listOf(
        {codeAnalysis()},
        {monitorSystemResources()}
    )

    abstract val envInitLines: List<String>

    suspend fun initEnvironment() = coroutineScope {
        val stuffToPrint = envInitLines.iterator()

        terminal.info(
            """
            ${(TextColors.brightYellow + TextStyles.bold)("🖥️ - Initializing Development Environment")}
                - Project: ${terminal.theme.success("Project")}
                - Environment: ${terminal.theme.success("Backend Development")}
                
            """.trimIndent()
        )

        val progress = progressBarLayout {
            spinner(Spinner.Dots())
            timeElapsed()
            progressBar()
            percentage()
            speed("s", style = terminal.theme.info)
        }.animateInCoroutine(terminal)

        progress.update { total = 50 }
        val job = launch { progress.execute() }

        while (!progress.finished) {
            delay((10..300).random().toLong())
            if (progress.completed%(50/(envInitLines.size-1)) == 0L) terminal.info(stuffToPrint.next())
            progress.advance(1)
        }

        job.join()
        println(" ")
    }

    abstract val classNamePrefix: List<String>
    abstract val classNameSuffix: List<String>
    abstract val analysis: List<Analysis>
    abstract val analysisResults: List<() -> String>

    protected val fileName: String
        get() = "${classNamePrefix.random()}${classNameSuffix.random()}.${fileExtension.random()}"


    suspend fun codeAnalysis() = coroutineScope {
        terminal.info((TextColors.cyan + TextStyles.bold)("🔎 - Running Code Analysis on API Components"))
        println(" ")

        val total = (5..26).random()
        val progress = progressBarLayout {
            spinner(Spinner.Dots())
            timeElapsed()
            progressBar()
            completed(suffix = " files")
            timeRemaining(style = terminal.theme.info)
        }.animateInCoroutine(terminal)

        progress.update { this.total = total.toLong() }
        val job = launch { progress.execute() }

        while (!progress.finished) {
            val a = analysis.random()
            val num = a.range.random()
            val (label, isGood) = a.labelFn(num)

            if (isGood) terminal.info("✅ - $fileName - ${a.name}: $num ($label)")
            else terminal.warn("⚠️ - $fileName - ${a.name}: $num ($label)")
            delay((10..700).random().toLong())
            progress.advance(1)
        }

        val results = mutableListOf<String>()
        val resIt = analysisResults.shuffled().iterator()
        for (i in 1 .. min(analysisResults.size, (2..10).random())) results.add(resIt.next()())

        job.join()
        println(" ")
        terminal.info(TextColors.cyan("📊 - Analysis Results: $total files, ${total * (200..2000).random()} lines of code"))
        for (r in results) println("    - $r")
        println(" ")

    }

    suspend fun monitorSystemResources() = coroutineScope {
        terminal.info((TextColors.brightMagenta + TextStyles.bold)("💽️ - Monitoring System Resources"))
        println(" ")

        val progress = progressBarLayout {
            spinner(Spinner.Dots())
            progressBar()
            timeElapsed()
        }.animateInCoroutine(terminal)

        progress.update { this.total = 10 }
        val job = launch { progress.execute() }

        var processes = (30..1000).random()
        while (!progress.finished) {
            terminal.info(
                listOf(
                    "CPU: ${(0..100).random()}%".padEnd(9),
                    "RAM: ${(0..100).random()}%".padEnd(9),
                    "NETWORK: ${(0..100).random()}%".padEnd(13),
                    "DISK: ${(0..100).random()}%".padEnd(10),
                    "PROCESSES: $processes".padEnd(16),
                ).joinToString(" | ")
            )

            processes += ((-100)..100).random()
            delay(1000)
            progress.advance(1)
        }

        job.join()
        println(" ")

        terminal.info(
            """
            ${TextColors.cyan("📊 - Resource Utilization:")}
                - Peak CPU: ${(0..100).random()}%
                - Peak Memory: ${(0..100).random()}%
                - Average Network Speed: ${(0..200).random()} MB/s
                - Average Disk Write Speed: ${(0..600).random()} MB/s
            """.trimIndent()
        )

        println(" ")
    }
}