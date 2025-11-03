package com.gabof92.hebrewaudiobible.usecases

import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.domain.WordPair
import javax.inject.Inject

class GetWordPairsUseCase @Inject constructor(private val repository: BibleRepository) {
    suspend operator fun invoke(book: Int, chapter: Int, verse: Int): List<WordPair> = repository.getWordPairs(book, chapter, verse)
}


