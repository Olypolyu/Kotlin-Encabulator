package org.example.data

object BackendDataSource : DataSource() {

    override val networkEndpoints: List<String> = listOf(
        "/api/v2/"
    )

    override val process: List<Pair<String, String?>> = listOf(
        Pair("pre-caching endpoints", null),
        Pair("updating docker containers", null),
        Pair("checking for vulnerabilities", null),
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