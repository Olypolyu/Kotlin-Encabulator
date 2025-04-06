package org.example.data

import com.github.ajalt.mordant.animation.coroutines.animateInCoroutine
import com.github.ajalt.mordant.animation.progress.advance
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.warning
import com.github.ajalt.mordant.widgets.Spinner
import com.github.ajalt.mordant.widgets.progress.*
import kotlinx.coroutines.*
import org.example.error
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
        {monitorSystemResources()},
        {analyzeAPIEndpoints()},
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

            val str = "$fileName - ${a.name}: $num${a.suffix} ($label)"
            if (isGood) terminal.info("✅ - $str") else terminal.warn("⚠️ - $str")

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

    val statusCodes: List<Int> = listOf(
        200, 201, 204, // 2xx Success
        301, 302, 304, // 3xx Redirection
        400, 401, 403, 404, 422, 429, // 4xx Client Error
        500, 502, 503, 504, // 5xx Server Error
    )

    abstract val apiEndpoints: List<String>
    suspend fun analyzeAPIEndpoints() = coroutineScope{
        terminal.info((TextColors.brightCyan + TextStyles.bold)("🕸️ - Analyzing API Traffic"))
        println(" ")

        val maxPadding = apiEndpoints.max().length
        val methods: List<String> = listOf(
            TextColors.brightGreen("GET"),
            TextColors.brightRed("DELETE"),
            TextColors.brightBlue("POST"),
            TextColors.brightMagenta("PATCH"),
            TextColors.brightYellow("PUT"),
        )

        val progress = progressBarLayout {
            spinner(Spinner.Dots())
            progressBar()
            timeElapsed()
        }.animateInCoroutine(terminal)

        progress.update { this.total = (10..15).random().toLong() }
        val job = launch { progress.execute() }

        while (!progress.finished) {
            val num = when ((1..10).random()) {
                in 1..6 -> statusCodes[(0..2).random()]
                7 -> statusCodes[(3..5).random()]
                8 -> statusCodes[(12..15).random()]
                in 9..10 -> statusCodes[(6..11).random()]
                else -> 0
            }

            val numStr:String = when {
                num >= 400 -> terminal.theme.danger(num.toString())
                num >= 300  -> terminal.theme.warning(num.toString())
                else -> terminal.theme.success(num.toString())
            }

            val method = methods.random().padEnd(16, ' ')
            val endpoint = apiEndpoints.random().padEnd(8 + maxPadding)
            val ms = "${(72..1000).random()}ms".padEnd(5)
            val size = "${(225..1000).random()}KB".padEnd(5)
            terminal.info("$method $endpoint -> $numStr | $ms | $size")

            delay((10..800).random().toLong())
            progress.advance(1)
        }

        job.join()
        println(" ")
        terminal.info(
            """
            ${TextColors.cyan("📊 Network Activity Summary:")}
                - Total requests: ${(0..1000).random()}
                - Average response time: ${(0..100).random()} ms
                - Success rate: ${(0..100).random()}%
                - Bandwidth utilization: ${(20..200).random()} MB/s
            """.trimIndent()
        )

        println(" ")
    }

}