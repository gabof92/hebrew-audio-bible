package com.gabof92.hebrewaudiobible.network

class BollsBibleDataSource {
    suspend fun getWordDefinitions(query: String): List<RootWord> {
        return BollsBibleApi.retrofitService.getWordDefinitions(query)
    }
}