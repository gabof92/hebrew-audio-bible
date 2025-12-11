package com.gabof92.hebrewaudiobible.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gabof92.hebrewaudiobible.domain.Book
import kotlinx.coroutines.launch

class ChapterSelectorDialogState(
    private val book: Book
) {
    var title by mutableStateOf(book.name)
    var showBookList by mutableStateOf(false)
    var showChapterGrid by mutableStateOf(true)
    var selectedBook by mutableStateOf<Book>(book)

    fun openBookList(){
        showBookList = true
        showChapterGrid = false
        title = "Select Book"
    }

    fun openChapters(){
        showBookList = false
        showChapterGrid = true
        title = selectedBook.name
    }
}

@Composable
fun rememberDialogState(book: Book): ChapterSelectorDialogState {
    return remember { ChapterSelectorDialogState(book) }
}

@Composable
fun ChapterSelectorDialog(
    book: Book,
    books: List<Book>,
    onChapterSelected: (Int, Int) -> Unit,
    onDismissRequest: () -> Unit,
    dialogState: ChapterSelectorDialogState = rememberDialogState(book)
) {
    Dialog(onDismissRequest = onDismissRequest) {
        // Scroll to the selected book
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(dialogState.showBookList) {
            if (dialogState.showBookList) {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = dialogState.selectedBook.number - 1)
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        dialogState.openBookList()
                    }
                ){
                    Text(
                        text = dialogState.title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            //.padding(bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }

                Column(){
                    if (dialogState.showBookList)
                        LazyColumn(
                            state = listState,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        ) {
                            items(books.size) { index ->
                                val book = books[index]
                                BookListItem(
                                    book = book,
                                    isSelected = book.number == dialogState.selectedBook.number,
                                    onClick = {
                                        dialogState.selectedBook = book
                                        dialogState.openChapters()
                                    },
                                )
                            }
                        }
                    if (dialogState.showChapterGrid)
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 64.dp),
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(dialogState.selectedBook.chapters) { index ->
                                val chapterNumber = index + 1
                                ChapterGridItem(
                                    chapterNumber = chapterNumber,
                                    onClick = {
                                        onChapterSelected(dialogState.selectedBook.number, chapterNumber)
                                        onDismissRequest()
                                    }
                                )
                            }
                        }
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun BookListItem(
    book: Book,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor =
        if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.background
    Row(
        Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(start = 16.dp, bottom = 8.dp, top = 8.dp, end = 16.dp)
            .combinedClickable(
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = book.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
    HorizontalDivider()
}

@Composable
fun ChapterGridItem(
    chapterNumber: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            // .size(60.dp) // Example fixed size, or use aspect ratio
            .aspectRatio(1f) // Makes items square
            .clickable(onClick = onClick)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = chapterNumber.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ChapterSelectorDialog(
            book = Book(19, "Psalms", 150),
            books = listOf(
                Book(1, "Genesis", 50),
                Book(2, "Exodus", 40),
                Book(19, "Psalms", 150),
            ),
            onChapterSelected = {_, _ -> },
            onDismissRequest = {}
        )
    }
}