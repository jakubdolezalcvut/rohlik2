package cz.rohlik.data

internal data class Articles(
    val results: List<Article>,
    val previous: String?,
    val next: String?,
)
