package cz.rohlik.network

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

internal data class Article(
    val id: Long,
    val title: String,
    val authors: List<Author>,
    val url: String,
    @SerializedName(value = "image_url") val imageUrl: String,
    val summary: String,
    @SerializedName(value = "published_at") val publishedAt: Instant,
)
