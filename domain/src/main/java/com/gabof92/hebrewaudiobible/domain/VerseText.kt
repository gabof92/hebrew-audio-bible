package com.gabof92.hebrewaudiobible.domain

data class VerseText(
    val verse: Int,
    val hebrew: String,
    val transliteration: String,
    val translation: String
)
