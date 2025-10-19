package com.gabof92.hebrewaudiobible.usecases

import com.gabof92.hebrewaudiobible.data.BibleRepository

class GetAudioUrlByChapterUseCase(private val repository: BibleRepository) {
    suspend operator fun invoke(book: Int, chapter: Int) = repository.getChapterAudioUrl(book, chapter)
}