package com.gabof92.hebrewaudiobible.ui

import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun WordDetailScreen(
    title: String,
    htmlContent: String,
    onDismissRequest: () -> Unit // You'll still need a way to "close" it
) {
    // This Box will act as the overlay/scrim
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)) // Semi-transparent background
            .padding(16.dp), // Padding around the "dialog" content
        contentAlignment = Alignment.Center // Center the dialog content on the screen
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Occupy 90% of screen width, for example
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Padding inside the card
            ) {
                Text(
                    text = "Root Word (${title})",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Composable to display HTML
                HtmlText(
                    html = htmlContent.convertStrongsLinks(),
                    modifier = Modifier.weight(
                        1f,
                        fill = false
                    )
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismissRequest) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

/**
 * A composable function that displays HTML-formatted text.
 * It uses an AndroidView to host a TextView because Compose's Text composable
 * does not directly support HTML rendering with all features like clickable links.
 */
@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                // Important for making links clickable
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView ->
            textView.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            if (style.fontSize != TextUnit.Unspecified) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.fontSize.value)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    WordDetailScreen(
        "H7225",
        htmlContent = "Original: \u003Cb\u003E\u003Che\u003Eראשׁית\u003C/he\u003E\u003C/b\u003E \u003Cp /\u003ETransliteration: \u003Cb\u003Erêshı̂yth\u003C/b\u003E \u003Cp /\u003EPhonetic: \u003Cb\u003Eray-sheeth\u003C/b\u003E \u003Cp class=\"bdb_def\"\u003E\u003Cb\u003EBDB Definition\u003C/b\u003E:\u003C/p\u003E\u003Col\u003E\u003Cli\u003Efirst, beginning, best, chief\u003Col type=a\u003E\u003Cli\u003Ebeginning\u003C/li\u003E\u003Cli\u003Efirst\u003C/li\u003E\u003Cli\u003Echief\u003C/li\u003E\u003Cli\u003Echoice part\u003C/li\u003E\u003C/ol\u003E\u003C/li\u003E\u003C/ol\u003E \u003Cp /\u003EOrigin: from the same as \u003Ca href=S:H7218\u003EH7218\u003C/a\u003E \u003Cp /\u003ETWOT entry: \u003Ca class=\"T\" href=\"S:2097 - rosh\"\u003E2097e\u003C/a\u003E \u003Cp /\u003EPart(s) of speech: Noun Feminine ",
        onDismissRequest = { }
    )
}

fun String.convertStrongsLinks(): String {
    val html = this
    val strongsRegex = Regex("""<a href=S:([HG])(\d+)>(.*?)</a>""")
    val twotRegex = Regex("""<a[^>]*href=["']S:\d+\s*-\s*[^"']*["'][^>]*>(.*?)</a>""")

    // Replace Strong's links with full URLs
    var updatedHtml = strongsRegex.replace(html) { match ->
        val prefix = match.groupValues[1]
        val number = match.groupValues[2]
        val label = match.groupValues[3]
        val url = "https://www.blueletterbible.org/lang/lexicon/lexicon.cfm?Strongs=$prefix$number&t=KJV"
        """<a href="$url">$label</a>"""
    }

    // Replace TWOT links with just their inner text (plain text)
    updatedHtml = twotRegex.replace(updatedHtml) { match ->
        match.groupValues[1]  // just the link text
    }

    return updatedHtml
}