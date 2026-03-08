package fr.isen.clara.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.isen.clara.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Search


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TheGreatestCocktailAppTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    @Composable
    fun RandomCocktailScreen() {
        var randomDrink by remember { mutableStateOf<Drink?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            try {
                val response = NetworkManager.api.getRandomDrink()
                randomDrink = response.drinks?.firstOrNull()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }

        when {
            isLoading -> {
                Text("Loading random cocktail...")
            }

            errorMessage != null -> {
                Text("Error: $errorMessage")
            }

            randomDrink != null -> {
                DetailCocktailScreen(
                    drink = randomDrink!!,
                    onBack = {}
                )
            }

            else -> {
                Text("No drink found")
            }
        }
    }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "random",
                    onClick = {
                        navController.navigate("random") {
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Random") },
                    label = { Text("Random") }
                )

                NavigationBarItem(
                    selected = currentRoute == "list",
                    onClick = {
                        navController.navigate("list") {
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "List") },
                    label = { Text("List") }
                )

                NavigationBarItem(
                    selected = currentRoute == "favorites",
                    onClick = {
                        navController.navigate("favorites") {
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = currentRoute == "search",
                    onClick = {
                        navController.navigate("search") {
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list"
        ) {
            composable("random") {
                RandomCocktailScreen()
            }

            composable("list") {
                CategoriesScreen(modifier = Modifier.padding(innerPadding))
            }

            composable("favorites") {
                    FavouritesScreen(modifier = Modifier.padding(innerPadding))
            }

            composable("search") {
                SearchScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}