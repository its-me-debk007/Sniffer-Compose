package `in`.sniffer.presentation

import `in`.sniffer.domain.models.ContactDetail
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Contacts(activity: Activity) {
    val contactsCollection by remember { mutableStateOf(getContacts(activity)) }
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {

        contactsCollection.forEach {
            stickyHeader {
                Text(
                    text = it.key.toString(),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .border(BorderStroke(2.dp, Color.LightGray), RoundedCornerShape(18.dp))
                        .padding(vertical = 6.dp)
                )
            }

            items(it.value.size) { idx ->
                ContactItem(it.value[idx])
            }
        }
    }
}

@Composable
private fun ContactItem(contactDetail: ContactDetail, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { isExpanded = !isExpanded }
    ) {
        Row {
//            Icon(
//                bitmap = contactDetail.image?.asImageBitmap() ?: Bitmap.createBitmap(
//                    120,
//                    120,
//                    Bitmap.Config.ARGB_8888
//                ).asImageBitmap(),
//                contentDescription = null,
//                modifier = Modifier.clip(CircleShape)
//                    .border(BorderStroke(2.dp, Color.Green), CircleShape)
//            )

//            Spacer(Modifier.width(8.dp))

            Text(
                text = contactDetail.name,
                overflow = if (!isExpanded) TextOverflow.Ellipsis else TextOverflow.Visible,
                maxLines = if (!isExpanded) 1 else Int.MAX_VALUE,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
            )
        }


        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(start = 24.dp)
            ) {
                contactDetail.numbers.forEach {
                    Text(text = it)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

fun getContacts(activity: Activity): Map<Char, List<ContactDetail>> {
    val contactDetails = mutableListOf<ContactDetail>()
    val contactsMap = mutableMapOf<String, MutableList<String>>()
    val photoMap = mutableMapOf<String, Bitmap?>()

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

        photoMap[name] = photo
    }

    contacts.close()

    contactsMap.forEach {
        contactDetails.add(ContactDetail(it.key, it.value, photoMap[it.key]))
    }

    return contactDetails.groupBy { it.name[0] }
}


