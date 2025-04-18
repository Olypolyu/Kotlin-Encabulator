package com.github.olypolyu.ktencabulator.data

import com.github.ajalt.mordant.animation.coroutines.animateInCoroutine
import com.github.ajalt.mordant.animation.progress.advance
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.Spinner
import com.github.ajalt.mordant.widgets.progress.*
import kotlinx.coroutines.*
import com.github.olypolyu.ktencabulator.JargonLevel
import com.github.olypolyu.ktencabulator.info
import com.github.olypolyu.ktencabulator.warn
import kotlin.math.min
import kotlin.random.Random

// TODO: Put better names, just added this to make code a bit clearer
data class Process(val text: String, val suffix: String?)

data class Analysis(val name: String, val range: IntRange, val suffix:String = "", val labelFn: (Int) -> Pair<String, Boolean>)

abstract class DataSource(
    val terminal: Terminal,
    val jargonLevel: JargonLevel,
    val projectName: String,
    val envName: String?,
)

{
    abstract val fileExtension: List<String>

    val tasks: List<suspend () -> Unit> = listOf(
        {codeAnalysis()},
        {monitorSystemResources()},
        {analyzeData()},
        {analyzeAPI()}
    )

    abstract val envType: String
    abstract val envInitLines: List<String>

    abstract val jargonMap: Map<JargonLevel, List<String>>

    fun fetchJargon(): String? = jargonMap[jargonLevel]?.random()

    suspend fun initEnvironment() = coroutineScope {
        val stuffToPrint = envInitLines.iterator()

        terminal.info(
            """
            ${(TextColors.brightYellow + TextStyles.bold)("\uD83D\uDDA5\uFE0F - Initializing Development Environment")}
                - Project: ${terminal.theme.success(projectName)}
                - Environment: ${terminal.theme.success(envName ?: envType)}
                
            """.trimIndent()
        )

        delay(900)

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
        terminal.println(" ")
    }

    abstract val classNamePrefix: List<String>
    abstract val classNameSuffix: List<String>
    abstract val analysis: List<Analysis>
    abstract val analysisResults: List<() -> String>

    protected val fileName: String
        get() = "${classNamePrefix.random()}${classNameSuffix.random()}.${fileExtension.random()}"

    suspend fun codeAnalysis() = coroutineScope {
        terminal.info((TextColors.cyan + TextStyles.bold)("\uD83D\uDD0E - Running Code Analysis on API Components"))

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

            "${if(isGood) "✅" else "⚠️"} - $fileName - ${a.name}: $num${a.suffix} ($label)".also {
                if (isGood) terminal.info(it)
                else terminal.warn(it)
            }

            delay((10..700).random().toLong())
            progress.advance(1)
        }

        val results = mutableListOf<String>()
        val resIt = analysisResults.shuffled().iterator()
        for (i in 1 .. min(analysisResults.size, (2..10).random())) results.add(resIt.next()())
        fetchJargon()?.also { results.add(it) }

        job.join()
        terminal.println(" ")
        terminal.info(TextColors.cyan("\uD83D\uDCCA - Analysis Results: $total files, ${total * (200..2000).random()} lines of code"))
        for (r in results) println("    - $r")
        terminal.println(" ")
    }

    suspend fun monitorSystemResources() = coroutineScope {
        terminal.info((TextColors.brightMagenta + TextStyles.bold)("\uD83D\uDCBD - Monitoring System Resources"))

        val progress = progressBarLayout {
            spinner(Spinner.Dots())
            progressBar()
            timeElapsed()
        }.animateInCoroutine(terminal)

        progress.update { this.total = 10 }
        val job = launch { progress.execute() }

        var processes = (30..1000).random()
        while (!progress.finished) {
            terminal.println(
                listOf(
                    "CPU: ${(0..100).random()}%".padEnd(9),
                    "RAM: ${(0..100).random()}%".padEnd(9),
                    "NETWORK: ${(0..100).random()}%".padEnd(13),
                    "DISK: ${(0..100).random()}%".padEnd(10),
                    "PROCESSES: $processes".padEnd(16),
                ).joinToString(" | ")
            )

            processes += ((-100)..100).random()
            delay(250)
            progress.advance(1)
        }

        job.join()
        terminal.println(" ")

        terminal.info(
            """
            ${TextColors.cyan("\uD83D\uDCCA - Resource Utilization:")}
                - Peak CPU: ${(0..100).random()}%
                - Peak Memory: ${(0..100).random()}%
                - Average Network Speed: ${(0..200).random()} MB/s
                - Average Disk Write Speed: ${(0..600).random()} MB/s
            """.trimIndent()
        )

        terminal.println(" ")
    }

    val statusCodes: List<Int> = listOf(
        200, 201, 204, // 2xx Success
        301, 302, 304, // 3xx Redirection
        400, 401, 403, 404, 422, 429, // 4xx Client Error
        500, 502, 503, 504, // 5xx Server Error
    )

    abstract val apiEndpoints: List<String>
    suspend fun analyzeAPI() = coroutineScope {
        terminal.info((TextColors.brightCyan + TextStyles.bold)("🕸️ - Analyzing API Endpoints"))

        val itr = apiEndpoints.shuffled().iterator()
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

        progress.update { this.total = ((if(apiEndpoints.size > 7) 5 else 0)..min(apiEndpoints.size, 15)).random().toLong()}
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
            val endpoint = itr.next().padEnd(8 + maxPadding)
            val ms = "${(72..1000).random()}ms".padEnd(5)
            val size = "${(225..1000).random()}KB".padEnd(5)
            terminal.println(" $method $endpoint -> $numStr | $ms | $size")

            delay((10..800).random().toLong())
            progress.advance(1)
        }

        job.join()
        terminal.println(" ")
        terminal.info(
            """
            ${TextColors.cyan("\uD83D\uDCCA - Network Activity Summary:")}
                - Total requests: ${(0..1000).random()}
                - Average response time: ${(0..100).random()} ms
                - Success rate: ${(0..100).random()}%
                - Bandwidth utilization: ${(20..200).random()} MB/s
            """.trimIndent()
        )

        terminal.println(" ")
    }

    open val processingJargonPrefix: List<String> = listOf(
        "Compressing",
        "Migrating",
        "Generating",
        "Optimizing",
        "Evaluating caches for",
        "Processing",
        "Executing",
        "Evaluating",
        "Applying"
    )

    open val processingJargonSuffix: List<String> = listOf(
        "reports",
        "indexes",
        "transactions",
        "replicas",
    )

    open val processingJargonSizes: List<String> = listOf("KB", "MB", "GB", "B")

    abstract val processJargon: List<String>
    abstract val processSubJargon: List<String>

    suspend fun analyzeData() = coroutineScope {
        terminal.info((TextColors.brightCyan + TextStyles.bold)("\uD83D\uDD03️ - Analyzing Data Traffic"))

        val progress = progressBarLayout {
            spinner(Spinner.Dots())
            timeElapsed()
            progressBar()
            completed()
            timeRemaining(style = terminal.theme.info)
        }.animateInCoroutine(terminal)

        progress.update { this.total = (2..10).random().toLong()}

        val job = launch { progress.execute() }
        while (!progress.finished) {
            val title = processJargon.random()
            val size = "(${(10..100).random()}${processingJargonSizes.random()})"
            val suffix = "${(1000..5000).random()} ${processingJargonSuffix.random()}"

            terminal.info("\uD83D\uDD03 ${processingJargonPrefix.random()} $title | $suffix $size")
            for (i in 0..(0..10).random()) {
                terminal.println(
                    "   - ${processingJargonPrefix.random()} ${processSubJargon.random()} ${(mutableListOf("logic") + processingJargonSuffix).random()}"
                )
                delay((10..200).random().toLong())
            }

            if (Random.nextBoolean()) terminal.println(terminal.theme.success("   ✅ ${fetchJargon()}"))
            delay((10..800).random().toLong())
            progress.advance(1)
        }

        job.join()
        terminal.println(" ")

        terminal.info(
            """
            ${TextColors.cyan("\uD83D\uDCCA - Data Processing Summary:")}
                - Records processed: ${(2500..10000).random()}
                - Processing rate: ${(100..5000).random()} records/sec
                - Total data size: ${(15..300).random()} ${processingJargonSizes.random()}
                - Estimated time saved: ${(25..100).random()} minutes
                - ${fetchJargon()}
        
            """.trimIndent()
        )
    }
}