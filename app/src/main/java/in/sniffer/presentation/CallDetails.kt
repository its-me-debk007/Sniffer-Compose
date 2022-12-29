package `in`.sniffer.presentation

import `in`.sniffer.MainActivity
import `in`.sniffer.domain.models.CallDetail
import android.provider.CallLog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun CallDetails() {
    val callDetails by remember { mutableStateOf(getCallDetails()) }

    LazyColumn(
        contentPadding = PaddingValues(32.dp)
    ) {
        items(callDetails.size) {
            CallDetailsItem(callDetail = callDetails[it])
        }
    }
}

@Composable
private fun CallDetailsItem(callDetail: CallDetail, modifier: Modifier = Modifier) {
    Column(modifier.padding(bottom = 32.dp)) {
        Text(text = callDetail.name)
        Text(text = callDetail.phoneNo)
        Text(text = callDetail.type)
        Text(text = callDetail.duration)
        Text(text = callDetail.date.toString())
    }
}

private fun getCallDetails(): List<CallDetail> {
    val activity = MainActivity()
    val callLogList = mutableListOf<CallDetail>()

    val callLogs = activity.contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        null,
        null,
        null,
        null
    )!!
    val nameColumnIndex = callLogs.getColumnIndex(CallLog.Calls.CACHED_NAME)
    val numberColumnIndex = callLogs.getColumnIndex(CallLog.Calls.NUMBER)
    val typeColumnIndex = callLogs.getColumnIndex(CallLog.Calls.TYPE)
    val dateColumnIndex = callLogs.getColumnIndex(CallLog.Calls.DATE)
    val durationColumnIndex = callLogs.getColumnIndex(CallLog.Calls.DURATION)

    while (callLogs.moveToNext()) {
        val name = callLogs.getString(nameColumnIndex)
        val phoneNo = callLogs.getString(numberColumnIndex)
        val type = callLogs.getString(typeColumnIndex)
        val dateMillis = callLogs.getString(dateColumnIndex).toLong()
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(dateMillis),
//                Instant.ofEpochMilli(System.currentTimeMillis()),
            ZoneId.systemDefault()
        )
        val duration = callLogs.getString(durationColumnIndex)
        val dir = when (type.toInt()) {
            CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
            CallLog.Calls.INCOMING_TYPE -> "INCOMING"
            else -> "MISSED"
        }

        callLogList.add(CallDetail(name, phoneNo, dir, date, duration))
    }

    callLogs.close()

    return callLogList
}