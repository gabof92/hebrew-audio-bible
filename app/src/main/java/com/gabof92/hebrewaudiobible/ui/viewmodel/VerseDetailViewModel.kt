package com.gabof92.hebrewaudiobible.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.data.WordPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VerseDetailViewModel(
    private val repository: BibleRepository,
    private val book: Int,
    private val chapter: Int,
    private val verse: Int,
) : ViewModel() {

    private val _bookName = MutableStateFlow<String>("")
    val bookName: StateFlow<String> = _bookName.asStateFlow()

    private val _wordList = MutableStateFlow<List<WordPair>>(emptyList())
    val wordList: StateFlow<List<WordPair>> = _wordList.asStateFlow()

    init {
        loadBook(book)
        loadWords(book, chapter, verse)
    }

    private fun loadBook(bookNumber: Int) {
        viewModelScope.launch {
            _bookName.value = repository.getBook(bookNumber).name
        }
    }

    fun loadWords(book: Int, chapter: Int, verse: Int) {
        viewModelScope.launch {
            val originalWords = repository.getVerseWords(book, chapter, verse)
            originalWords.forEach { word ->
                val bestDefinition =
                    repository.getWordDefinitions("H${word.strongsHeb}")
                        .maxBy { it.weight }
                _wordList.value = _wordList.value +
                        WordPair(word, bestDefinition)
            }
        }
    }

    fun sortWordsByHebrew() {
        _wordList.value = _wordList.value.sortedBy { it.originalWord.hebrewSort }
    }

    fun sortWordsByEnglish() {
        _wordList.value = _wordList.value.sortedBy { it.originalWord.englishSort }
    }
}

class VerseDetailViewModelFactory(
    private val repository: BibleRepository,
    private val book: Int,
    private val chapter: Int,
    private val verse: Int,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerseDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VerseDetailViewModel(repository, book, chapter, verse) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}