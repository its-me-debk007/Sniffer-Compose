package `in`.sniffer.presentation

import android.app.Activity
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Contacts(activity: Activity) {
    val contactsMap by remember { mutableStateOf(getContacts(activity)) }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 32.dp, horizontal = 24.dp)
    ) {
        contactsMap.forEach {
            item {
                ContactItem(
                    name = it.key,
                    numbers = it.value,
                )
            }
        }
    }
}

@Composable
private fun ContactItem(name: String, numbers: List<String>, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Text(
            text = name,
            overflow = if (!isExpanded) TextOverflow.Ellipsis else TextOverflow.Visible,
            maxLines = if (!isExpanded) 1 else Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                numbers.forEach {
                    Text(text = it)
                    Spacer(Modifier.height(4.dp))
                }

                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "call",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Green)
                        .padding(6.dp)
                )
            }
        }
    }
}

fun getContacts(activity: Activity): Map<String, List<String>> {
    val contactsMap = mutableMapOf<String, MutableList<String>>()
    val contacts = activity.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )!!

    val nameColumnIndex =
        contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
    val numberColumnIndex =
        contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
    val photoColumnIndex =
        contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

    while (contacts.moveToNext()) {
        val name = contacts.getString(nameColumnIndex)
        val number = contacts.getString(numberColumnIndex).filterNot { it.isWhitespace() }
        val photoUri = contacts.getString(photoColumnIndex)

        val photo = try {
            if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(activity.contentResolver, Uri.parse(photoUri))
            } else {
                val source =
                    ImageDecoder.createSource(activity.contentResolver, Uri.parse(photoUri))
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: java.lang.Exception) {
            null
        }

        contactsMap[name]?.add(number) ?: run {
            contactsMap[name] = mutableListOf(number)
        }
    }

    contacts.close()

    return contactsMap
}


