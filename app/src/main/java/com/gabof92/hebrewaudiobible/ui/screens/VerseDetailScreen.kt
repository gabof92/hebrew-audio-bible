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
import com.gabof92.hebrewaudiobible.data.BibleRoomRepository
import com.gabof92.hebrewaudiobible.database.OriginalWord
import com.gabof92.hebrewaudiobible.ui.HtmlInfoScreen
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
    repository: BibleRoomRepository,
) {
    composable<VerseDetailScreen> {
        val args = it.toRoute<VerseDetailScreen>()
        val viewModel: VerseDetailViewModel =
            viewModel(
                factory = VerseDetailViewModelFactory(
                    repository, args.book, args.chapter, args.verse
                )
            )
        val bookName by viewModel.bookName.collectAsStateWithLifecycle()
        val wordList by viewModel.wordList.collectAsStateWithLifecycle()

        VerseDetailScreen(
            bookName,
            args.chapter,
            args.verse,
            wordList,
            onHebSortCLick = {viewModel.sortWordsByHebrew()},
            onEngSortCLick = {viewModel.sortWordsByEnglish()},
        )
    }
}

@Composable
fun VerseDetailScreen(
    bookName: String,
    chapterNumber: Int,
    verseNumber: Int,
    wordList: List<OriginalWord>,
    onHebSortCLick: () -> Unit = {},
    onEngSortCLick: () -> Unit = {},
) {
    var showHtmlInfo by remember { mutableStateOf(false) }
    val bottomBannerHeight = 56.dp

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                TopBanner(
                    text = "$bookName $chapterNumber:$verseNumber",
                    //onTextCLick = {}
                )
                LazyColumn(
                    contentPadding = PaddingValues(bottom = bottomBannerHeight),
                    modifier = Modifier.weight(1f)
                ) {

                    items(wordList) { word ->
                        WordItem(
                            word,
                            onItemClick = { showHtmlInfo = true }
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
            if (showHtmlInfo) {
                HtmlInfoScreen(
                    "H7225",
                    htmlContent = "Original: \u003Cb\u003E\u003Che\u003Eראשׁית\u003C/he\u003E\u003C/b\u003E \u003Cp /\u003ETransliteration: \u003Cb\u003Erêshı̂yth\u003C/b\u003E \u003Cp /\u003EPhonetic: \u003Cb\u003Eray-sheeth\u003C/b\u003E \u003Cp class=\"bdb_def\"\u003E\u003Cb\u003EBDB Definition\u003C/b\u003E:\u003C/p\u003E\u003Col\u003E\u003Cli\u003Efirst, beginning, best, chief\u003Col type=a\u003E\u003Cli\u003Ebeginning\u003C/li\u003E\u003Cli\u003Efirst\u003C/li\u003E\u003Cli\u003Echief\u003C/li\u003E\u003Cli\u003Echoice part\u003C/li\u003E\u003C/ol\u003E\u003C/li\u003E\u003C/ol\u003E \u003Cp /\u003EOrigin: from the same as \u003Ca href=S:H7218\u003EH7218\u003C/a\u003E \u003Cp /\u003ETWOT entry: \u003Ca class=\"T\" href=\"S:2097 - rosh\"\u003E2097e\u003C/a\u003E \u003Cp /\u003EPart(s) of speech: Noun Feminine ",
                    onDismissRequest = { showHtmlInfo = false }
                )
            }
        }
    }
}

@Composable
private fun WordItem(
    databaseWord: OriginalWord,
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
            Text(text = "H${databaseWord.strongsHeb}", style = MaterialTheme.typography.titleSmall)
            Text(text = "bollsAPI", style = MaterialTheme.typography.bodyMedium)
            Text(text = "bollsAPI", style = MaterialTheme.typography.bodyMedium)
        }
        VerticalDivider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(.375f)
                .padding(bottom = 8.dp, top = 8.dp),
        ) {
            Text(text = databaseWord.transliteration, style = MaterialTheme.typography.titleMedium)
            Text(text = databaseWord.original, style = MaterialTheme.typography.titleSmall)
            Text(text = databaseWord.parsingShort, style = MaterialTheme.typography.bodySmall)
        }
        VerticalDivider()
        Column(
            modifier = Modifier.weight(.375f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = databaseWord.translation,
                style = MaterialTheme.typography.titleMedium,
                //modifier = Modifier.weight(.5f),
            )
            HorizontalDivider()
            Text(
                text = "lit. " + "bollsAPI",
                style = MaterialTheme.typography.bodyMedium,
                //modifier = Modifier.weight(.5f),
            )
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