package com.gabof92.hebrewaudiobible.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.domain.Book
import com.gabof92.hebrewaudiobible.domain.WordPair
import com.gabof92.hebrewaudiobible.usecases.GetBookUseCase
import com.gabof92.hebrewaudiobible.usecases.GetWordPairsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val book: Book? = null,
    val chapter: Int,
    val verse: Int,
    val wordList: List<WordPair> = emptyList(),
)

@HiltViewModel
class VerseDetailViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val getWordPairsUseCase: GetWordPairsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val book: Int = checkNotNull(savedStateHandle["book"])
    private val chapter: Int = checkNotNull(savedStateHandle["chapter"])
    private val verse: Int = checkNotNull(savedStateHandle["verse"])

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