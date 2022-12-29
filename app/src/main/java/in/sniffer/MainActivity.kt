package `in`.sniffer

import `in`.sniffer.domain.BottomNavScreen
import `in`.sniffer.domain.BottomNavigation
import `in`.sniffer.ui.theme.SnifferComposeTheme
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnifferComposeTheme {
                val navHostController = rememberNavController()

//                val permissionLauncher = rememberLauncherForActivityResult(
//                    ActivityResultContracts.RequestPermission()
//                ) {
//
//                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

//                    when {
//                        ContextCompat.checkSelfPermission(
//                            this, Manifest.permission.READ_CONTACTS
//                        ) ==
//                                PackageManager.PERMISSION_DENIED -> {
//                            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
//                        }
//                        shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
//
//                        }
//                        else -> {
//                            BottomNavigation(navHostController = navHostController)
//                        }
//                    }

                    Scaffold(
                        bottomBar = { BottomBar(navController = navHostController) }
                    ) {
                        BottomNavigation(navHostController = navHostController, it)
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomBar(navController: NavHostController) {
        val screens = listOf(
            BottomNavScreen.ContactsScreen,
            BottomNavScreen.RecentsScreen
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar {
            screens.forEach { screen ->
                NavigationBarItem(
                    label = {
                        Text(text = screen.title)
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = "Navigation Icon"
                        )
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

    }

    fun getContacts(): Map<String, List<String>> {
//    val activity = MainActivity()
        val contactsMap = mutableMapOf<String, MutableList<String>>()
        val contacts = contentResolver.query(
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
                    MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photoUri))
                } else {
                    val source =
                        ImageDecoder.createSource(contentResolver, Uri.parse(photoUri))
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
}