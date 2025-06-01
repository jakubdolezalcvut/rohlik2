package cz.rohlik.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface SpaceflightService {

    @GET("v4/articles")
    suspend fun getArticles(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String?,
    ): Articles

    @GET("v4/articles/{id}")
    suspend fun getArticle(
        @Path("id") id: Long,
    ): Article
}
