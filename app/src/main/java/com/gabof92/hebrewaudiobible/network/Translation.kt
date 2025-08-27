package com.gabof92.hebrewaudiobible.network

import com.squareup.moshi.Json

data class Translation(
    @Json(name = "topic") val strong: String,
    @Json(name = "definition") val longDefinition: String,
    @Json(name = "lexeme") val originalWord: String,
    @Json(name = "transliteration") val transliteration: String,
    @Json(name = "pronunciation") val pronunciation: String,
    @Json(name = "short_definition") val definition: String,
    @Json(name = "weight") val weight: Double,
)