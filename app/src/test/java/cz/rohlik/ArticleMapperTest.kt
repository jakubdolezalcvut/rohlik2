package cz.rohlik

import cz.rohlik.network.ArticleMapper
import cz.rohlik.network.Author
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import okhttp3.HttpUrl.Companion.toHttpUrl
import cz.rohlik.network.Article as DataArticle
import cz.rohlik.domain.Article as DomainArticle

internal class ArticleMapperTest : FunSpec ({

    val mapper = ArticleMapper(
        timeZone = TimeZone.UTC,
    )
    val dataArticle = DataArticle(
        id = 123,
        publishedAt = Instant.parse("2025-06-01T12:12:00Z"),
        authors = listOf(
            Author(
                name = "Jakub Doležal",
            ),
        ),
        title = "Obří nutrie obchází Prahou",
        summary = "Obří nutrie byla opet spatřena na Střeleckém ostrově.",
        url = "http://rohlik.cz",
        imageUrl = "http://rohlik.cz/image",
    )

    val domainArticle = DomainArticle(
        id = 123,
        published = LocalDateTime.parse("2025-06-01T12:12:00"),
        authors = persistentListOf("Jakub Doležal"),
        title = "Obří nutrie obchází Prahou",
        summary = "Obří nutrie byla opet spatřena na Střeleckém ostrově.",
        url = "http://rohlik.cz".toHttpUrl(),
        imageUrl = "http://rohlik.cz/image".toHttpUrl(),
    )

    test("happy path") {
        mapper.map(dataArticle) shouldBe domainArticle
    }

    test("new line in summary is removed") {
        val article = dataArticle.copy(summary = "\n ${dataArticle.summary}")
        mapper.map(article) shouldBe domainArticle
    }
})
