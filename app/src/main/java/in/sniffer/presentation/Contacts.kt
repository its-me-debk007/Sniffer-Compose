package `in`.sniffer.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Contacts() {
//    val contactsMap by remember { mutableStateOf(MainActivity().getContacts()) }
    val contactsMap by remember { mutableStateOf(mapOf<String, List<String>>()) }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 32.dp, horizontal = 24.dp)
    ) {
        contactsMap.forEach {
            item {
                ContactItem(name = it.key, numbers = it.value)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactItem(name: String, numbers: List<String>, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        onClick = {
            isExpanded = !isExpanded
        }
    ) {
        Column(modifier = modifier) {
            Text(
                text = name,
                overflow = if (!isExpanded) TextOverflow.Ellipsis else TextOverflow.Visible,
                maxLines = if (!isExpanded) 1 else Int.MAX_VALUE
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = isExpanded) {
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

