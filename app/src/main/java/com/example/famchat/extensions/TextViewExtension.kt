package com.example.famchat.extensions

import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.famchat.R



fun TextView.spanText(
    textContent: String,
    textHighLights: List<String> = emptyList(),
    textBold: String = "",
    unLineText: Boolean = false,
    color: Int? = null,
    onClick: (() -> Unit)? = null
) {
    if (TextUtils.isEmpty(textContent)) return

    val span = SpannableString(textContent)

    textHighLights.forEach { part ->
        val startIndex = textContent.indexOf(part)
        if (startIndex != -1) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClick?.invoke()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = unLineText
                    ds.color = color ?: resources.getColor(R.color.ca_color_primary)
                }
            }

            span.setSpan(
                onClick?.let { clickableSpan } ?: ForegroundColorSpan(color ?: resources.getColor(R.color.ca_color_primary)),
                startIndex,
                startIndex + part.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    val startIndexBold = textContent.indexOf(textBold)
    if (startIndexBold != -1) {
        span.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndexBold,
            startIndexBold + textBold.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.text = span
    this.highlightColor = context.resources.getColor(R.color.ca_color_primary_highlight)
    this.movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        this.isGone = true
    } else {
        this.isVisible = true
        this.text = text
    }
}

fun TextView.setTextUnderLine(text: String) {
    val content = SpannableString(text)
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    setText(content)
}