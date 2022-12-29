package `in`.sniffer.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object ContactsScreen : BottomNavScreen(
        title = "Contacts",
        icon = Icons.Default.Person,
        route = "CONTACTS"
    )

    object RecentsScreen : BottomNavScreen(
        title = "Recent",
        icon = Icons.Default.Phone,
        route = "RECENTS"
    )
}