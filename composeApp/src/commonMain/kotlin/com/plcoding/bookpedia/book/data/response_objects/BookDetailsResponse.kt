@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSerializationApi::class)

package com.plcoding.bookpedia.book.data.response_objects

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class BookDetailsResponseObject(
    @Serializable(with = BookDescriptionSerializer::class) val description: String? = null,
    @SerialName("subjects") val subjects: List<String>? = null
)

object BookDetailsSerializer: KSerializer<BookDetailsResponseObject> {
    override val descriptor = buildClassSerialDescriptor(BookDetailsResponseObject::class.simpleName!!){
        element<String?>("description")
    }

    override fun deserialize(decoder: Decoder): BookDetailsResponseObject =
        decoder.decodeStructure(descriptor) {
            var description: String? = null

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> description = decodeSerializableElement(descriptor, index, BookDescriptionSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    CompositeDecoder.UNKNOWN_NAME -> continue
                }
            }

            BookDetailsResponseObject(description)
        }

    override fun serialize(encoder: Encoder, value: BookDetailsResponseObject) =
        encoder.encodeStructure(descriptor){
            value.description?.let { encodeStringElement(descriptor, 0, it) }
        }
}

object BookDescriptionSerializer : KSerializer<String?> {
    override val descriptor = PrimitiveSerialDescriptor("description", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) =
        when (val element = (decoder as? JsonDecoder)?.decodeJsonElement()) {
            is JsonObject -> element["value"]?.jsonPrimitive?.content
            is JsonPrimitive -> element.contentOrNull
            null -> throw SerializationException("This decoder only works with JSON.")
            else -> null
        }

    override fun serialize(encoder: Encoder, value: String?) {
        value?.let { encoder.encodeString(it) }
    }
}

/** Add [KSerializer]s, like [BookDescriptionSerializer], here. */

