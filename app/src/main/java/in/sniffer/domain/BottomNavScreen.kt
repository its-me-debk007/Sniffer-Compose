package `in`.sniffer.domain

import `in`.sniffer.R
import androidx.annotation.DrawableRes

sealed class BottomNavScreen(
    val title: String,
    @DrawableRes val outlinedIcon: Int,
    @DrawableRes val filledIcon: Int,
    val route: String
) {
    object ContactsScreen : BottomNavScreen(
        title = "Contacts",
        outlinedIcon = R.drawable.ic_contacts_outlined,
        filledIcon = R.drawable.ic_contacts_filled,
        route = "CONTACTS"
    )

    object CallsScreen : BottomNavScreen(
        title = "Calls",
        outlinedIcon = R.drawable.ic_calls_outlined,
        filledIcon = R.drawable.ic_calls_filled,
        route = "CALLS"
    )
}