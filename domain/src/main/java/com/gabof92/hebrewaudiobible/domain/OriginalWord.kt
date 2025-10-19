package com.gabof92.hebrewaudiobible.domain

data class OriginalWord(
    val id: Int,
    val book :Int,
    val chapter :Int,
    val verse :Int,
    val strongsHeb :Int,
    val original :String,
    val transliteration :String,
    val translation :String,
    val parsingShort :String,
    val parsingLong :String,
    val hebrewSort :Int,
    val englishSort :Int,
)
