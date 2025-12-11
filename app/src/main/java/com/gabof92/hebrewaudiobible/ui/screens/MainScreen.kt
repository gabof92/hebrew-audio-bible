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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gabof92.hebrewaudiobible.domain.VerseText
import com.gabof92.hebrewaudiobible.presentation.viewmodel.MainUiState
import com.gabof92.hebrewaudiobible.presentation.viewmodel.MainViewModel
import com.gabof92.hebrewaudiobible.ui.ChapterSelectorDialog
import com.gabof92.hebrewaudiobible.ui.theme.HebrewAudioBibleTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
object MainScreen

class MainScreenState() {

    var showChapterDialog by mutableStateOf(false)

    fun openChapterDialog() {
        showChapterDialog = true
    }

    fun dismissChapterDialog() {
        showChapterDialog = false
    }
}

@Composable
fun rememberMainScreenState(): MainScreenState {
    return remember { MainScreenState() }
}

fun NavGraphBuilder.mainScreenDestination(
    onNavigateToVerseDetail: (book: Int, chapter: Int, verse: Int) -> Unit
) {
    composable<MainScreen> {

        val viewModel: MainViewModel = hiltViewModel()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        MainScreen(
            uiState = uiState,
            onNavigateToVerseDetail = onNavigateToVerseDetail,
            onChapterChange = { newBook, newChapter ->
                viewModel.changeChapter(newBook, newChapter)
            },
            nextChapter = { viewModel.nextChapter() },
            previousChapter = { viewModel.previousChapter()},
            onVerseClick = { verseNumber -> viewModel.seekAudioByVerse(verseNumber) },
            onTogglePlay = { viewModel.togglePlay() },
            onRestartAudio = { viewModel.seekAudio(0) },
            onToggleTextVisibility = viewModel::updateTextVisibility
        )
    }
}

@Composable
fun MainScreen(
    screenState: MainScreenState = rememberMainScreenState(),
    uiState: MainUiState,
    onNavigateToVerseDetail: (book: Int, chapter: Int, verse: Int) -> Unit,
    onChapterChange: (Int, Int) -> Unit,
    nextChapter: () -> Unit,
    previousChapter: () -> Unit,
    onTogglePlay: () -> Unit,
    onRestartAudio: () -> Unit,
    onVerseClick: (Int) -> Unit = {},
    onToggleTextVisibility: (Boolean?, Boolean?, Boolean?) -> Unit
) {

    val bottomBannerHeight = 56.dp

    // Scroll to the item when currentAudioVerse changes
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(uiState.currentAudioVerse) {
        if (uiState.currentAudioVerse > 0 && uiState.currentAudioVerse <= uiState.verses.size) {
            val targetVerseIndex = uiState.currentAudioVerse - 1
            coroutineScope.launch {
                listState.animateScrollToItem(index = targetVerseIndex)
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
                    text = "${uiState.book.name} ${uiState.chapter}",
                    onLeftCLick = previousChapter,
                    onTextCLick = screenState::openChapterDialog,
                    onRightClick = nextChapter
                )

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = bottomBannerHeight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(uiState.verses) { verse ->
                        VerseListItem(
                            verse = verse,
                            showHebrewText = uiState.showHebrewText,
                            showTransliteration = uiState.showTransliteration,
                            showEnglishText = uiState.showEnglishText,
                            onItemClick = { onVerseClick(verse.verse) },
                            onItemLongClick = {
                                onNavigateToVerseDetail(
                                    uiState.book.number,
                                    uiState.chapter,
                                    verse.verse
                                )
                            },
                            currentAudioVerse = uiState.currentAudioVerse
                        )
                    }
                }
            }

            BottomBanner(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(bottomBannerHeight),
                uiState = uiState,
                onTogglePlayClick = onTogglePlay,
                isAudioPlaying = uiState.isAudioPlaying,
                onRestartClick = onRestartAudio,
                onToggleTextVisibility = onToggleTextVisibility
            )

            if (screenState.showChapterDialog) {
                ChapterSelectorDialog(
                    book = uiState.book,
                    books = uiState.books,
                    onChapterSelected = onChapterChange,
                    onDismissRequest = screenState::dismissChapterDialog
                )
            }
        }
    }
}

@Composable
fun VerseListItem(
    modifier: Modifier = Modifier,
    verse: VerseText,
    showHebrewText: Boolean = true,
    showTransliteration: Boolean = true,
    showEnglishText: Boolean = true,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit,
    currentAudioVerse: Int,
) {
    val backgroundColor =
        if (currentAudioVerse == verse.verse)
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
            text = verse.verse.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column {
            if (showHebrewText) Text(
                text = verse.hebrew,
                style = MaterialTheme.typography.titleMedium
            )
            if (showTransliteration) Text(
                text = verse.transliteration,
                style = MaterialTheme.typography.titleMedium
            )
            if (showEnglishText) Text(
                text = verse.translation,
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
    uiState: MainUiState,
    isAudioPlaying: Boolean,
    onTogglePlayClick: () -> Unit,
    onRestartClick: () -> Unit,
    onToggleTextVisibility: (Boolean?, Boolean?, Boolean?) -> Unit
) {
    var expandMenu by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            //.height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Restart Button
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
        // Play Button
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
        // Dropdown Menu
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // Each Box gets equal weight
                .combinedClickable(onClick = { expandMenu = !expandMenu })
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
                expanded = expandMenu,
                onDismissRequest = { expandMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Hebrew Text") },
                    trailingIcon = {
                        Icon(
                            imageVector =
                                if (uiState.showHebrewText) Icons.Default.Check
                                else Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    onClick = { onToggleTextVisibility(!uiState.showHebrewText, null, null) },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("Transliteration") },
                    trailingIcon = {
                        Icon(
                            imageVector =
                                if (uiState.showTransliteration)
                                    Icons.Default.Check
                                else Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    onClick = { onToggleTextVisibility(null, !uiState.showTransliteration, null) },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("English Text") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (uiState.showEnglishText) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    onClick = { onToggleTextVisibility(null, null, !uiState.showEnglishText) },
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
