package com.gabof92.hebrewaudiobible.data.network

import com.gabof92.hebrewaudiobible.data.RemoteDataSource
import com.gabof92.hebrewaudiobible.domain.RootWord

class BollsBibleDataSource : RemoteDataSource {
    override suspend fun getWordDefinition(strongsNumber: Int): RootWord {
        return BollsBibleApi.retrofitService
            .getWordDefinitions("H$strongsNumber")
            .toDomainModel()
    }
}

/* Mappers */

private fun RootWordResult.toDomainModel(): RootWord = RootWord(
    strongs = strongs.replace(Regex("\\D"), "") //Removing letter
        .toInt(),
    longDefinition = longDefinition,
    shortDefinition = shortDefinition,
    hebrewWord = hebrewWord,
    transliteration = transliteration,
    pronunciation = pronunciation,
)

private fun List<RootWordResult>.toDomainModel(): RootWord =
    maxBy { it.weight }.toDomainModel() //Getting best definition from list