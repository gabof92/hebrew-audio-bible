package com.gabof92.hebrewaudiobible.domain

data class VerseTimeStamp(
    val book: Int,
    val chapter: Int,
    val verse: Int,
    val millis: Int,
)
