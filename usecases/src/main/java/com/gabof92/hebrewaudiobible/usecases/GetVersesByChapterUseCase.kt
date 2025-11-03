package com.gabof92.hebrewaudiobible.usecases

import com.gabof92.hebrewaudiobible.data.BibleRepository
import javax.inject.Inject

class GetVersesByChapterUseCase @Inject constructor(private val repository: BibleRepository) {
    suspend operator fun invoke(book: Int, chapter: Int) = repository.getVerses(book, chapter)
}