package org.example.data

object BackendDataSource : DataSource() {

    override val networkEndpoints: List<String> = listOf(
        "/api/v2/"
    )

    override val process: List<Process> = listOf(
        Process("pre-caching endpoints", null),
        Process("updating docker containers", null),
        Process("checking for vulnerabilities", null),
    )

    override val softJargon: List<String>
        get() = TODO("Not yet implemented")

    override val mediumJargon: List<String>
        get() = TODO("Not yet implemented")

    override val hardJargon: List<String>
        get() = TODO("Not yet implemented")

    override val boiledJargon: List<String>
        get() = TODO("Not yet implemented")
}