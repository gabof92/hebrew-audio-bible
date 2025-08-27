package com.gabof92.hebrewaudiobible.network

class BollsBibleDataSource {
    suspend fun getTranslation(query: String): List<Translation> {
        return BollsBibleApi.retrofitService.getTranslation(query)
    }
}