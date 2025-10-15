package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.database.OriginalWord
import com.gabof92.hebrewaudiobible.database.RootWord

data class WordPair(
    val originalWord: OriginalWord,
    val rootWord: RootWord,
)
