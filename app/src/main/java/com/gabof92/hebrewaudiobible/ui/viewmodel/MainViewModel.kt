package com.gabof92.hebrewaudiobible.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.data.BibleRoomRepository
import com.gabof92.hebrewaudiobible.data.VerseText
import com.gabof92.hebrewaudiobible.database.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: BibleRoomRepository,
) : ViewModel() {
    // Private MutableStateFlow that will hold the current list of verses
    private val _verseList = MutableStateFlow<List<VerseText>>(emptyList())

    // Public immutable StateFlow that Composables can collect
    val verseList: StateFlow<List<VerseText>> = _verseList.asStateFlow()

    private val _currentBook = MutableStateFlow<Book>(
        Book(19, "Psalms", 150)
    )
    val currentBook: StateFlow<Book> = _currentBook.asStateFlow()

    private val _currentChapterNumber = MutableStateFlow(1) // Default to chapter 1
    val currentChapter: StateFlow<Int> = _currentChapterNumber.asStateFlow()

    init {
        // Initial data load
        loadVerses(_currentBook.value.number, _currentChapterNumber.value)
    }

    // Function to load verses for a specific book and chapter
    fun loadVerses(bookNumber: Int, chapter: Int) {
        viewModelScope.launch {
            // Update current book and chapter if they change
            val book = repository.getBook(bookNumber)
            _currentBook.value = book
            _currentChapterNumber.value = chapter
            // Fetch data from the repository
            val verses = repository.getVerses(bookNumber, chapter) // Ensure this is a suspend function
            _verseList.value = verses
        }
    }

}

class MovieViewModelFactory(private val repository: BibleRoomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}