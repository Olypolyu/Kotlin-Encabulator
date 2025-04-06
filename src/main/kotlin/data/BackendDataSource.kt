package org.example.data

import com.github.ajalt.mordant.terminal.Terminal

class BackendDataSource(terminal: Terminal) : DataSource(terminal) {
    override val fileExtension: List<String> = listOf(
        "ts",
        "js",
        "kt",
        "java",
        "scala",
        "groovy",
    )

    override val envInitLines: List<String> = listOf(
        "Loading Configuration files...",
        "Syncing with remotes...",
        "Downloading Libraries...",
        "Validating Security Certificates...",
        "Connecting to Database Server...",
        "Initializing Modules...",
        terminal.theme.success("✅ Project Initialization Complete.")
    )

    override val classNamePrefix: List<String> = listOf(
        "account",
        "user",
        "store",
        "notification",
        "product",
        "asset",
        "internationalization",
        "data",
        "telemetry",
    )

    override val classNameSuffix: List<String> = listOf(
        "Model",
        "Controller",
        "Manager",
        "Factory",
        "Queue",
        "Adapter",
        "Repository",
        "Prototype",
    )
    override val analysis: List<Analysis> = listOf(

        Analysis("Linter::Nesting Depth", (0..10)) {
            when (it) {
                in 0..3 -> Pair("good", true)
                in 4..6 -> Pair("okay", true)
                else    -> Pair("poor", false)
            }
        },

        Analysis("Linter::Code Quality", (0..100)) {
            when (it) {
                in 0..25 -> Pair("bad", false)
                in 26..50 -> Pair("poor", false)
                in 51..75 -> Pair("fine", true)
                in 76..100 -> Pair("good", true)

                else    -> Pair("", false)
            }
        },

        Analysis("Memory Usage", 0..4096, "MB") {
            when (it) {
                in 0..255 -> Pair("great", true)
                in 255..555 -> Pair("good", true)
                in 555..2048 -> Pair("fine", true)
                in 2048..3072 -> Pair("poor", false)
                in 3072..4096 -> Pair("bad", false)

                else    -> Pair("", false)
            }
        }

    )

    override val analysisResults: List<() -> String> = listOf(
        {"Issues found: ${(0..10).random()}"},
        {"Code Quality: ${(75..100).random()}%"},
        {"Technical Debt: ${(0..10).random()}%"}
    )

    override val apiEndpoints: List<String> = listOf(
        "api/v2/internal/admin",
        "api/v2/internal/dashboard",
        "api/v2/bot",
        "api/v2/users",
        "api/v2/users/{id}",
        "api/v2/auth",
        "api/v2/auth/login",
        "api/v2/repo/notify",
        "api/v2/repo/updates",
        "api/v2/telemetry",
        "api/v2/content",
    )
}