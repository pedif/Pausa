package com.techys.onboarding.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun boldIntroductionText(text: String, delimiter: String = "—"): AnnotatedString {
       return buildAnnotatedString {
            val parts = text.split(delimiter)
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(parts[0])
            }
            append(" — ${parts[1]}")
        }
}