package `in`.sniffer.domain.models

import android.graphics.Bitmap

data class Contact(
    val name: String,
    val number: List<String>,
    val image: Bitmap? = null
)
