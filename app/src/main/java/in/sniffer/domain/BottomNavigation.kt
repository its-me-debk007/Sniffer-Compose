package `in`.sniffer.domain

import `in`.sniffer.presentation.Contacts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavigation(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = BottomNavScreen.ContactsScreen.route
    ) {
        composable(BottomNavScreen.ContactsScreen.route) {
            Contacts()
        }

        composable(BottomNavScreen.RecentsScreen.route) {

        }
    }
}