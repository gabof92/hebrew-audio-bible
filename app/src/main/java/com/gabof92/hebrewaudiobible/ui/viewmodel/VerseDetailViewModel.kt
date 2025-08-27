package com.gabof92.hebrewaudiobible.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.database.OriginalWord
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

    private val _wordList = MutableStateFlow<List<OriginalWord>>(emptyList())
    val wordList: StateFlow<List<OriginalWord>> = _wordList.asStateFlow()

    private val _bookName = MutableStateFlow<String>("")
    val bookName: StateFlow<String> = _bookName.asStateFlow()

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
            val words = repository.getVerseWords(book, chapter, verse)
            _wordList.value = words
        }
    }

    fun sortWordsByHebrew() {
        _wordList.value = _wordList.value.sortedBy { it.hebrewSort }
    }

    fun sortWordsByEnglish() {
        _wordList.value = _wordList.value.sortedBy { it.englishSort }
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