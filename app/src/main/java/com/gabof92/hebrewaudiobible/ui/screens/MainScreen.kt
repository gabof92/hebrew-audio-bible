package com.gabof92.hebrewaudiobible.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.data.VerseText
import com.gabof92.hebrewaudiobible.database.Book
import com.gabof92.hebrewaudiobible.ui.ChapterSelectorDialog
import com.gabof92.hebrewaudiobible.ui.theme.HebrewAudioBibleTheme
import com.gabof92.hebrewaudiobible.ui.viewmodel.MainViewModel
import com.gabof92.hebrewaudiobible.ui.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
object MainScreen

fun NavGraphBuilder.mainScreenDestination(
    repository: BibleRepository,
    onNavigateToVerseDetail: (book: Int, chapter: Int, verse: Int) -> Unit
) {
    composable<MainScreen> {

        val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(repository))

        val currentBook by viewModel.currentBook.collectAsStateWithLifecycle()
        val currentChapter by viewModel.currentChapter.collectAsStateWithLifecycle()
        val verseList by viewModel.verseList.collectAsStateWithLifecycle()
        val isAudioPlaying by viewModel.isAudioPlaying.collectAsStateWithLifecycle()
        val currentAudioVerse by viewModel.currentAudioVerse.collectAsStateWithLifecycle()

        MainScreen(
            verseList = verseList,
            book = currentBook,
            chapter = currentChapter,
            isAudioPlaying = isAudioPlaying,
            currentAudioVerse = currentAudioVerse,
            onNavigateToVerseDetail = onNavigateToVerseDetail,
            onChapterChange = { newChapter ->
                viewModel.loadVerses(viewModel.currentBook.value.number, newChapter)
            },
            onVerseClick = { verseNumber ->
                viewModel.seekAudioByVerse(verseNumber)
            },
            onTogglePlay = {
                if (isAudioPlaying) viewModel.pauseAudio()
                else viewModel.playAudio()
            },
            onRestartAudio = {
                viewModel.seekAudio(0)
            },
        )
    }
}

@Composable
fun MainScreen(
    verseList: List<VerseText>,
    book: Book,
    chapter: Int,
    onNavigateToVerseDetail: (book: Int, chapter: Int, verse: Int) -> Unit,
    onChapterChange: (Int) -> Unit,
    onTogglePlay: () -> Unit,
    onRestartAudio: () -> Unit,
    isAudioPlaying: Boolean,
    onVerseClick: (Int) -> Unit = {},
    currentAudioVerse: Int,
) {

    var showHebrewText by remember { mutableStateOf(true) }
    var showTransliteration by remember { mutableStateOf(true) }
    var showEnglishText by remember { mutableStateOf(true) }
    val bottomBannerHeight = 56.dp
    var showChapterSelector by remember { mutableStateOf(false) }

    // Scroll to the item when currentAudioVerse changes
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(currentAudioVerse) {
        if (currentAudioVerse > 0 && currentAudioVerse <= verseList.size) {
            val indexToScroll = currentAudioVerse - 1
            coroutineScope.launch {
                listState.animateScrollToItem(index = indexToScroll)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                TopBanner(
                    text = "${book.name} $chapter",
                    onLeftCLick = {
                        val previousChapter = chapter - 1
                        if (previousChapter >= 1)
                            onChapterChange(previousChapter)
                    },
                    onTextCLick = { showChapterSelector = true },
                    onRightClick = {
                        val nextChapter = chapter + 1
                        if (nextChapter <= book.chapters)
                            onChapterChange(nextChapter)
                    }
                )

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = bottomBannerHeight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(verseList) { verse ->
                        VerseListItem(
                            verseNumber = verse.verse,
                            showHebrewText = showHebrewText,
                            showTransliteration = showTransliteration,
                            showEnglishText = showEnglishText,
                            hebrewText = verse.hebrew,
                            transliteration = verse.transliteration,
                            englishText = verse.translation,
                            onItemClick = { onVerseClick(verse.verse) },
                            onItemLongClick = {
                                onNavigateToVerseDetail(book.number, chapter, verse.verse)
                            },
                            currentAudioVerse = currentAudioVerse
                        )
                    }
                }
            }

            BottomBanner(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(bottomBannerHeight),
                showHebrewText = showHebrewText,
                showTransliteration = showTransliteration,
                showEnglishText = showEnglishText,
                onToggleHebrewClick = { showHebrewText = !showHebrewText },
                onToggleTransClick = {
                    showTransliteration = !showTransliteration
                },
                onToggleEnglishClick = { showEnglishText = !showEnglishText },
                onTogglePlayClick = onTogglePlay,
                isAudioPlaying = isAudioPlaying,
                onRestartClick = onRestartAudio
            )

            if (showChapterSelector) {
                ChapterSelectorDialog(
                    chapters = book.chapters,
                    onChapterSelected = onChapterChange,
                    onDismissRequest = { showChapterSelector = false }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HebrewAudioBibleTheme {
        //MainScreen(onNavigateToVerseDetail = { _, _, _ -> })
    }
}

@Composable
fun VerseListItem(
    modifier: Modifier = Modifier,
    verseNumber: Int,
    showHebrewText: Boolean = true,
    showTransliteration: Boolean = true,
    showEnglishText: Boolean = true,
    hebrewText: String = "",
    transliteration: String = "",
    englishText: String = "",
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit,
    currentAudioVerse: Int,
) {
    val backgroundColor =
        if (currentAudioVerse == verseNumber)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.background
    Row(
        modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(start = 16.dp, bottom = 8.dp, top = 8.dp, end = 16.dp)
            .combinedClickable(
                onClick = { onItemClick() },
                onLongClick = { onItemLongClick() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = verseNumber.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column {
            if (showHebrewText) Text(
                text = hebrewText,
                style = MaterialTheme.typography.titleMedium
            )
            if (showTransliteration) Text(
                text = transliteration,
                style = MaterialTheme.typography.titleMedium
            )
            if (showEnglishText) Text(
                text = englishText,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
    HorizontalDivider()
}

@Composable
private fun TopBanner(
    modifier: Modifier = Modifier,
    text: String = "",
    onLeftCLick: () -> Unit = {},
    onTextCLick: () -> Unit = {},
    onRightClick: () -> Unit = {},
) {
    Row(
        modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(.3f) // Each Box gets equal weight
                .minimumInteractiveComponentSize()
                .combinedClickable(onClick = onLeftCLick),
            contentAlignment = Alignment.Center // Center the icon within this Box
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // Each Box gets equal weight
                .minimumInteractiveComponentSize()
                .combinedClickable(onClick = onTextCLick),
            contentAlignment = Alignment.Center // Center the icon within this Box
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(.3f) // Each Box gets equal weight
                .minimumInteractiveComponentSize()
                .combinedClickable(onClick = onRightClick),
            contentAlignment = Alignment.Center // Center the icon within this Box
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun BottomBanner(
    modifier: Modifier = Modifier,
    showHebrewText: Boolean = true,
    showTransliteration: Boolean = true,
    showEnglishText: Boolean = true,
    onToggleHebrewClick: () -> Unit = {},
    onToggleTransClick: () -> Unit = {},
    onToggleEnglishClick: () -> Unit = {},
    onRestartClick: () -> Unit = {},
    onTogglePlayClick: () -> Unit = {},
    isAudioPlaying: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            //.height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // Each Box gets equal weight
                .combinedClickable(onClick = onRestartClick)
                .minimumInteractiveComponentSize(),
            contentAlignment = Alignment.Center // Center the icon within this Box
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // Each Box gets equal weight
                .combinedClickable(onClick = onTogglePlayClick)
                .minimumInteractiveComponentSize(),
            contentAlignment = Alignment.Center // Center the icon within this Box
        ) {
            Icon(
                imageVector =
                    if (isAudioPlaying) Icons.Filled.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // Each Box gets equal weight
                .combinedClickable(onClick = { expanded = !expanded })
                .minimumInteractiveComponentSize(),
            contentAlignment = Alignment.Center // Center the icon within this Box
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Hebrew Text") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showHebrewText) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    onClick = onToggleHebrewClick,
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("Transliteration") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showTransliteration) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    onClick = onToggleTransClick,
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("English Text") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showEnglishText) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    onClick = onToggleEnglishClick,
                )
            }
        }
    }
}