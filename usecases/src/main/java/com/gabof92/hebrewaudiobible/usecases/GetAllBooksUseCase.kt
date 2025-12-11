package com.gabof92.hebrewaudiobible.usecases

import com.gabof92.hebrewaudiobible.data.BibleRepository
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(private val repository: BibleRepository) {
    suspend operator fun invoke() = repository.getAllBooks()
}