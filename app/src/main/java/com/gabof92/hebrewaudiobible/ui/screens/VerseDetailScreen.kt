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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.toRoute
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.data.WordPair
import com.gabof92.hebrewaudiobible.network.RootWord
import com.gabof92.hebrewaudiobible.ui.viewmodel.DetailUiState
import com.gabof92.hebrewaudiobible.ui.viewmodel.VerseDetailViewModel
import com.gabof92.hebrewaudiobible.ui.viewmodel.VerseDetailViewModelFactory
import kotlinx.serialization.Serializable

@Serializable
data class VerseDetailScreen(
    val book: Int,
    val chapter: Int,
    val verse: Int,
)

fun NavGraphBuilder.verseDetailScreenDestination(
    repository: BibleRepository,
) {
    composable<VerseDetailScreen> {
        val args = it.toRoute<VerseDetailScreen>()
        val viewModel: VerseDetailViewModel =
            viewModel(
                factory = VerseDetailViewModelFactory(
                    repository, args.book, args.chapter, args.verse
                )
            )
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        VerseDetailScreenContent(
            uiState = uiState,
            onHebSortCLick = { viewModel.sortWordsByHebrew() },
            onEngSortCLick = { viewModel.sortWordsByEnglish() },
        )
    }
}

@Composable
fun VerseDetailScreenContent(
    uiState: DetailUiState,
    onHebSortCLick: () -> Unit = {},
    onEngSortCLick: () -> Unit = {},
) {
    var selectedWord by remember { mutableStateOf<RootWord?>(null) }
    val bottomBannerHeight = 56.dp

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                TopBanner(

                    text =
                        if (uiState.book != null)
                            "${uiState.book.name} ${uiState.chapter}:${uiState.verse}"
                        else "Loading...",
                    //onTextCLick = {}
                )
                LazyColumn(
                    contentPadding = PaddingValues(bottom = bottomBannerHeight),
                    modifier = Modifier.weight(1f)
                ) {

                    items(uiState.wordList) { word ->
                        WordItem(
                            word,
                            onItemClick = { selectedWord = word.rootWord }
                        )
                    }
                }
            }
            BottomBanner(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(bottomBannerHeight),
                onHebSortCLick = onHebSortCLick,
                onEngSortCLick = onEngSortCLick,
            )
            selectedWord?.let {
                WordDetailScreen(
                    word = it,
                    onDismissRequest = { selectedWord = null }
                )
            }
        }
    }
}

@Composable
private fun WordItem(
    word: WordPair,
    onItemClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .combinedClickable(
                onClick = { onItemClick() },
            ),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(.25f),
        ) {
            Text(
                text = "H${word.originalWord.strongsHeb}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(text = word.rootWord.hebrewWord, style = MaterialTheme.typography.bodyMedium)
            Text(text = word.rootWord.transliteration, style = MaterialTheme.typography.bodyMedium)
        }
        VerticalDivider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(.375f)
                .padding(bottom = 8.dp, top = 8.dp),
        ) {
            Text(
                text = word.originalWord.transliteration,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = word.originalWord.original, style = MaterialTheme.typography.titleSmall)
            Text(text = word.originalWord.parsingShort, style = MaterialTheme.typography.bodySmall)
        }
        VerticalDivider()
        Column(
            modifier = Modifier.weight(.375f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = word.originalWord.translation,
                style = MaterialTheme.typography.titleMedium,
                //modifier = Modifier.weight(.5f),
            )
            /*HorizontalDivider()
            Text(
                text = "(${word.rootWord.shortDefinition})",
                style = MaterialTheme.typography.bodyMedium,
                //modifier = Modifier.weight(.5f),
            )*/
        }
    }
    HorizontalDivider(
        thickness = 2.5.dp,
    )
}

@Composable
private fun TopBanner(
    modifier: Modifier = Modifier,
    text: String = "",
    //onTextCLick: () -> Unit = {},
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
                .weight(1f)
                .minimumInteractiveComponentSize()
            //.combinedClickable(onClick = onTextCLick),
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun BottomBanner(
    modifier: Modifier = Modifier,
    onHebSortCLick: () -> Unit = {},
    onEngSortCLick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .combinedClickable(onClick = onHebSortCLick)
                .minimumInteractiveComponentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Hebrew Sort", style = MaterialTheme.typography.titleMedium)
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .combinedClickable(onClick = onEngSortCLick)
                .minimumInteractiveComponentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "English Sort", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    //HebrewAudioBibleTheme { VerseDetailScreen(VerseDetailScreen(1, 1, 1)) }
}