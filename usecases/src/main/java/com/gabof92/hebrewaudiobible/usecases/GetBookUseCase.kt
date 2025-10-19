package com.gabof92.hebrewaudiobible.usecases

import com.gabof92.hebrewaudiobible.data.BibleRepository

class GetBookUseCase(private val repository: BibleRepository) {
    suspend operator fun invoke(bookNumber: Int) = repository.getBook(bookNumber)
}