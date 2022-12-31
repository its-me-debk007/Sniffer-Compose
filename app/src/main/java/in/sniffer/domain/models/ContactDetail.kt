package `in`.sniffer.domain.models

import android.graphics.Bitmap

data class ContactDetail(
    val name: String,
    val numbers: List<String>,
    val image: Bitmap? = null
)
