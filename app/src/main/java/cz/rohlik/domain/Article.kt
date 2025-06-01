package cz.rohlik.domain

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Serializable
@Immutable
internal data class Article(
    val id: Long,
    val published: LocalDateTime,
    val authors: ImmutableList<String>,
    val title: String,
    val summary: String,
    @Serializable(with = HttpUrlSerializer::class) val url: HttpUrl,
    @Serializable(with = HttpUrlSerializer::class) val imageUrl: HttpUrl,
)

internal object HttpUrlSerializer : KSerializer<HttpUrl> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("cz.rohlik.HttpUrlAsString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, httpUrl: HttpUrl) {
        encoder.encodeString(httpUrl.toString())
    }

    override fun deserialize(decoder: Decoder): HttpUrl = decoder.decodeString().toHttpUrl()
}
