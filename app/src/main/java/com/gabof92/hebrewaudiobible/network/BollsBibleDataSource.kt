package com.gabof92.hebrewaudiobible.network

class BollsBibleDataSource {
    suspend fun getWordDefinitions(query: String): List<RootWordResult> {
        return BollsBibleApi.retrofitService.getWordDefinitions(query)
    }
}