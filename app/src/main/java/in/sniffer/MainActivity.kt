package `in`.sniffer

import `in`.sniffer.domain.BottomNavScreen
import `in`.sniffer.domain.BottomNavigation
import `in`.sniffer.ui.theme.SnifferComposeTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
                        bottomBar = { BottomBar(navController = navHostController) },
                        topBar = {  }
                    ) {
                        BottomNavigation(
                            navHostController = navHostController,
                            this@MainActivity,
                            it
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchBar() {

    }

    @Composable
    private fun BottomBar(navController: NavHostController) {
        val screens = listOf(
            BottomNavScreen.CallsScreen,
            BottomNavScreen.ContactsScreen
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar {
            screens.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true

                NavigationBarItem(
                    label = {
                        Text(text = screen.title)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = if (selected) screen.filledIcon else screen.outlinedIcon),
                            contentDescription = null
                        )
                    },
                    selected = selected,
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
}