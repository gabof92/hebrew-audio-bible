package com.gabof92.hebrewaudiobible.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.domain.Book
import com.gabof92.hebrewaudiobible.domain.WordPair
import com.gabof92.hebrewaudiobible.usecases.GetBookUseCase
import com.gabof92.hebrewaudiobible.usecases.GetWordPairsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val book: Book? = null,
    val chapter: Int,
    val verse: Int,
    val wordList: List<WordPair> = emptyList(),
)

class VerseDetailViewModel(
    private val getBookUseCase: GetBookUseCase,
    private val getWordPairsUseCase: GetWordPairsUseCase,
    book: Int,
    chapter: Int,
    verse: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DetailUiState(
            chapter = chapter,
            verse = verse,
        )
    )
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadData(book, chapter, verse)
    }

    fun loadData(bookNumber: Int, chapter: Int, verse: Int) {
        viewModelScope.launch {
            _uiState.value.copy(isLoading = true)
            try {
                val book = getBookUseCase(bookNumber)
                val wordPairs = getWordPairsUseCase(bookNumber, chapter, verse)
                _uiState.update {
                    it.copy(
                        book = book,
                        chapter = chapter,
                        verse = verse,
                        wordList = wordPairs,
                        isLoading = false,
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load data"
                    )
                }
            }
        }
    }

    fun sortWordsByHebrew() {
        val sortedList = uiState.value.wordList.sortedBy { it.originalWord.hebrewSort }
        _uiState.update { it.copy(wordList = sortedList) }
    }

    fun sortWordsByEnglish() {
        val sortedList = uiState.value.wordList.sortedBy { it.originalWord.englishSort }
        _uiState.update { it.copy(wordList = sortedList) }
    }
}

class VerseDetailViewModelFactory(
    private val getBookUseCase: GetBookUseCase,
    private val getWordPairsUseCase: GetWordPairsUseCase,
    private val book: Int,
    private val chapter: Int,
    private val verse: Int,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerseDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VerseDetailViewModel(
                getBookUseCase,
                getWordPairsUseCase,
                book,
                chapter,
                verse,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}