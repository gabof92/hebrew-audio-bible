package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.domain.RootWord

interface RemoteDataSource {
    suspend fun getWordDefinition(strongsNumber: Int): RootWord
}