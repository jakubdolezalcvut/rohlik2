package cz.rohlik.ui.screens

import cz.rohlik.domain.Article
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import okhttp3.HttpUrl.Companion.toHttpUrl

internal val article = Article(
    id = 123,
    published = LocalDateTime.parse("2025-06-01T12:12"),
    authors = persistentListOf("Jakub Doležal"),
    title = "Obří nutrie obchází Prahou",
    summary = "Obří nutrie byla opet spatřena na Střeleckém ostrově skupinkou korejských turistů dnes ráno. Nutrie dlouhá včetně ocasu celý metr posnídala housky se sýrem a odplavala směrem k Vyšehradu. Budeme dále informovat.",
    url = "http://rohlik.cz".toHttpUrl(),
    imageUrl = "http://rohlik.cz/image".toHttpUrl(),
)

internal val articles = List(5) { id -> article.copy(id = id.toLong()) }
    .toImmutableList()
