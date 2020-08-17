package com.yogesh.twitterdemo.extras

import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
 * Summarize the number to a fancy [String]
 */
fun Int.summarizeNumber(): String {
    val firstPart: Int
    val secondPart: Int

    // for millions
    if (this >= 1000000) {
        firstPart = this / 1000000
        secondPart = (this - firstPart * 1000000)

        if (secondPart < 100000) return "${firstPart}M"

        return "$firstPart.${secondPart / 100000}M"
    } else if (this >= 1000) {
        // for thousands
        firstPart = this / 1000
        secondPart = (this - firstPart * 1000)

        if (firstPart in 10..99) {
            if (secondPart < 100) return "${firstPart}K"

            return "$firstPart.${secondPart / 100}K"
        } else if (firstPart >= 100) {
            return "${firstPart}K"
        }

        // check the hundred part
        if (secondPart < 10) return "$firstPart.00$secondPart"

        if (secondPart < 100) return "$firstPart.0$secondPart"

        return "$firstPart.$secondPart"
    }

    return this.toString()
}

/**
 * Summarize the number to counts items
 */
fun Int.summarizeCountNumber() = if (this == 0) "" else this.summarizeNumber()

/**
 * Highlight citations and urls in text
 */
fun String.highlightClickable(): Spanned {

    val reCitation = Regex("""([@#]\w+)""") // regex for citations and hashtags
    val reLinks = Regex("""(http\S+)""")    // regex to links
    val reEMails = Regex("""(\w+@\S+)""")   // regex to e-mails
    var textParsed = reEMails.replace(this, """<font color="#1DA1F2">$1</font>""")

    textParsed = reCitation.replace(textParsed, """<font color="#1DA1F2">$1</font>""")
    textParsed = reLinks.replace(textParsed, """<font color="#1DA1F2">$1</font>""")

    return HtmlCompat.fromHtml(textParsed, HtmlCompat.FROM_HTML_MODE_COMPACT)
}
