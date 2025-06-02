package cz.rohlik.network

internal data class Articles(
    val results: List<Article>,
    val previous: String?,
    val next: String?,
)
