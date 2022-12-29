package `in`.sniffer.domain.models

import java.time.LocalDateTime

data class CallDetail(
    val name: String,
    val phoneNo: String,
    val type: String,
    val date: LocalDateTime,
    val duration: String
)
