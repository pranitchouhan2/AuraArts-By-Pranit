package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.AppViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Gallery : Screen("gallery", "Gallery", Icons.Default.Photo)
    object Shop : Screen("shop", "Shop", Icons.Default.ShoppingBag)
    object Reels : Screen("reels", "Reels", Icons.Default.Movie)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val customDarkMode by viewModel.isDarkMode.collectAsState()

            val useDarkTheme = when (customDarkMode) {
                true -> true
                false -> false
                else -> isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = useDarkTheme, dynamicColor = false) {
                AuraArtsApp(viewModel = viewModel, useDarkTheme = useDarkTheme)
            }
        }
    }
}

@Composable
fun AuraArtsApp(
    viewModel: AppViewModel,
    useDarkTheme: Boolean
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Gallery,
        Screen.Shop,
        Screen.Reels,
        Screen.Profile
    )

    // Detemine if the current screen requires bottom bar or not
    val currentRoute = currentDestination?.route
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .frostedGlass(RoundedCornerShape(24.dp), useDarkTheme),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    tint = if (selected) AuraPurple else Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    color = if (selected) AuraPurple else Color.Gray,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = AuraPurple.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(
                bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            composable("splash") {
                SplashScreen(
                    onSplashComplete = {
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("onboarding") {
                OnboardingScreens(
                    onOnboardingComplete = {
                        navController.navigate("home") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToGallery = { category ->
                        viewModel.updateGalleryCategory(category)
                        navController.navigate("gallery") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToDetail = { id ->
                        navController.navigate("detail/$id")
                    },
                    onNavigateToCommissions = {
                        navController.navigate("commissions")
                    },
                    onNavigateToReels = {
                        navController.navigate("reels")
                    },
                    onNavigateToNotifications = {
                        navController.navigate("notifications")
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("gallery") {
                GalleryScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { id ->
                        navController.navigate("detail/$id")
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("shop") {
                ShopScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { id ->
                        navController.navigate("detail/$id")
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("reels") {
                ReelsScreen(
                    viewModel = viewModel,
                    useDarkTheme = useDarkTheme
                )
            }

            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToGallery = { cat ->
                        viewModel.updateGalleryCategory(cat)
                        navController.navigate("gallery")
                    },
                    onNavigateToContact = {
                        navController.navigate("contact")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable(
                route = "detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "shiva_1"
                DetailScreen(
                    artworkId = id,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToDetail = { newId ->
                        navController.navigate("detail/$newId") {
                            popUpTo("detail/{id}") { inclusive = true }
                        }
                    },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("commissions") {
                CommissionScreen(
                    viewModel = viewModel,
                    useDarkTheme = useDarkTheme
                )
            }

            composable("contact") {
                ContactScreen(
                    onBackClick = { navController.popBackStack() },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("notifications") {
                NotificationsScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    useDarkTheme = useDarkTheme
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    useDarkTheme = useDarkTheme
                )
            }
        }
    }
}
