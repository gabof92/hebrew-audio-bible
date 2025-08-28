package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.database.OriginalWord
import com.gabof92.hebrewaudiobible.network.RootWord

data class WordPair(
    val originalWord: OriginalWord,
    val rootWord: RootWord,
)
