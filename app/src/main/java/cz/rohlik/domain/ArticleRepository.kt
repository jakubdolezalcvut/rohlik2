package cz.rohlik.domain

import cz.rohlik.data.ArticleMapper
import cz.rohlik.data.SpaceflightService
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class ArticleRepository(
    private val dispatchers: Dispatchers,
    private val mapper: ArticleMapper,
    private val service: SpaceflightService,
) {
    suspend fun loadArticles(
        offset: Int,
        limit: Int,
        search: String?,
    ): Result<ImmutableList<Article>> = withContext(dispatchers.Default) {
        runCatching {
            val articles = service.getArticles(
                offset = offset,
                limit = limit,
                search = search,
            )
            articles.results.map(mapper::map).toImmutableList()
        }
            .onFailure { throwable ->
                Timber.e(throwable, "Unable to load articles")
            }
    }

    suspend fun loadArticle(
        articleId: Long,
    ): Result<Article> = withContext(dispatchers.Default) {
        runCatching {
            val article = service.getArticle(
                id = articleId,
            )
            mapper.map(article)
        }
            .onFailure { throwable ->
                Timber.e(throwable, "Unable to load article $articleId")
            }
    }
}
