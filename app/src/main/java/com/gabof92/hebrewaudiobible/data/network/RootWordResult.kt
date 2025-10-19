package com.gabof92.hebrewaudiobible.data.network

import com.squareup.moshi.Json

data class RootWordResult(
    @Json(name = "topic") val strongs: String,
    @Json(name = "definition") val longDefinition: String,
    @Json(name = "lexeme") val hebrewWord: String,
    @Json(name = "transliteration") val transliteration: String,
    @Json(name = "pronunciation") val pronunciation: String,
    @Json(name = "short_definition") val shortDefinition: String,
    @Json(name = "weight") val weight: Double,
)