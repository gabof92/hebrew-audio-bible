package com.gabof92.hebrewaudiobible.usecases

import com.gabof92.hebrewaudiobible.data.BibleRepository

class GetTimestampsByChapterUseCase(private val repository: BibleRepository) {
    suspend operator fun invoke(book: Int, chapter: Int) = repository.getChapterTimestamps(book, chapter)
}