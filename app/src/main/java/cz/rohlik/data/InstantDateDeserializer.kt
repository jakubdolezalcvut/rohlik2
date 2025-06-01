package cz.rohlik.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.Instant
import java.lang.reflect.Type

internal object InstantDateDeserializer : JsonDeserializer<Instant> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): Instant? =
        json?.asString?.let {
            Instant.parse(it)
        }
}
