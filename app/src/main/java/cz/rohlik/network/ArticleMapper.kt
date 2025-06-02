package cz.rohlik.network
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import okhttp3.HttpUrl.Companion.toHttpUrl
import cz.rohlik.network.Article as DataArticle
import cz.rohlik.domain.Article as DomainArticle

internal class ArticleMapper(
    private val timeZone: TimeZone,
) {
    private companion object {
        val EMPTY_LINE = "^\\s+".toRegex()
    }

    fun map(article: DataArticle): DomainArticle =
        DomainArticle(
            id = article.id,
            published = article.publishedAt.toLocalDateTime(timeZone),
            authors = article.authors.map(),
            title = article.title,
            summary = article.summary.removeUnwantedNewLine(),
            url = article.url.toHttpUrl(),
            imageUrl = article.imageUrl.toHttpUrl(),
        )

    private fun List<Author>.map(): ImmutableList<String> =
        map(Author::name).toImmutableList()

    private fun String.removeUnwantedNewLine(): String =
        replace(EMPTY_LINE, "")
}
