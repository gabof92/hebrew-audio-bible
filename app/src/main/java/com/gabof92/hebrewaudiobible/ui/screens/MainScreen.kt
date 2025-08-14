package com.gabof92.hebrewaudiobible.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gabof92.hebrewaudiobible.ui.ChapterSelectorDialog
import com.gabof92.hebrewaudiobible.ui.theme.HebrewAudioBibleTheme
import kotlinx.serialization.Serializable


@Serializable
object MainScreen

fun NavGraphBuilder.mainScreenDestination(
    onNavigateToVerseDetail: (book: Int, chapter: Int, verse: Int) -> Unit
) {
    composable<MainScreen> {
        MainScreen(onNavigateToVerseDetail)
    }
}

@Composable
fun MainScreen(onNavigateToVerseDetail: (book: Int, chapter: Int, verse: Int) -> Unit) {

    val context = LocalContext.current
    var showHebrewText by remember { mutableStateOf(true) }
    var showTransliteration by remember { mutableStateOf(true) }
    var showEnglishText by remember { mutableStateOf(true) }
    val bottomBannerHeight = 56.dp
    var showChapterSelector by remember { mutableStateOf(false) }
    var totalChapters by remember { mutableIntStateOf(128) }
    var currentChapter by remember { mutableIntStateOf(1) }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                TopBanner(
                    text = "Genesis ${currentChapter}",
                    onLeftCLick = {},
                    onTextCLick = { showChapterSelector = true },
                    onRightClick = {})
                LazyColumn(
                    contentPadding = PaddingValues(bottom = bottomBannerHeight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(50) { index ->
                        VerseListItem(
                            verseNumber = index + 1,
                            showHebrewText = showHebrewText,
                            showTransliteration = showTransliteration,
                            showEnglishText = showEnglishText,
                            hebrewText = "בְּרֵאשִׁ֖ית בָּרָ֣א אֱלֹהִ֑ים אֵ֥ת הַשָּׁמַ֖יִם וְאֵ֥ת הָאָֽרֶץ׃",
                            transliteration = "bə·rê·šîṯ bā·rā ’ĕ·lō·hîm; ’êṯ haš·šā·ma·yim wə·’êṯ hā·’ā·reṣ.",
                            englishText = "In the beginning God created the heavens and the earth",
                            onItemClick = {
                                Toast.makeText(
                                    context,
                                    "play audio verse: ${index + 1}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onItemLongClick = {
                                onNavigateToVerseDetail(1, currentChapter, index + 1)
                            }
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
            )
            if (showChapterSelector) {
                ChapterSelectorDialog(
                    chapters = totalChapters,
                    onChapterSelected = {
                        currentChapter = it
                        Toast.makeText(context, "Selected chapter: $it", Toast.LENGTH_SHORT)
                            .show()
                    },
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
        MainScreen(onNavigateToVerseDetail = { _, _, _ -> })
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
) {
    Row(
        modifier
            .fillMaxWidth()
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
    onRestartCLick: () -> Unit = {},
    onTogglePlayClick: () -> Unit = {},
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
                .combinedClickable(onClick = onRestartCLick)
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
                imageVector = Icons.Default.PlayArrow,
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