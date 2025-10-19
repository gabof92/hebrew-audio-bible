package com.gabof92.hebrewaudiobible.domain

data class RootWord(
    val language: String = "H",
    val strongs: Int,
    val longDefinition: String,
    val shortDefinition: String,
    val hebrewWord: String,
    val transliteration: String,
    val pronunciation: String,
)