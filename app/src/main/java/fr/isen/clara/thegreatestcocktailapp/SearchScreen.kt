package fr.isen.clara.thegreatestcocktailapp

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var drinks by remember { mutableStateOf<List<Drink>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasSearched by remember { mutableStateOf(false) }
AppBackground {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                errorMessage = null
            },
            label = { Text("Search cocktail by name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (query.isBlank()) return@Button

                scope.launch {
                    try {
                        isLoading = true
                        errorMessage = null
                        hasSearched = true

                        val response = NetworkManager.api.searchCocktailByName(query)
                        drinks = response.drinks ?: emptyList()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Unknown error"
                        drinks = emptyList()
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = query.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Text("Searching...")
            }

            errorMessage != null -> {
                Text("Error: $errorMessage")
            }

            !hasSearched -> {
                Text("Type a cocktail name to search")
            }

            drinks.isEmpty() -> {
                Text("No results found")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(drinks) { drink ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    val intent = Intent(context, DrinkDetailActivity::class.java)
                                    intent.putExtra(
                                        DrinkDetailActivity.EXTRA_DRINK_ID,
                                        drink.idDrink
                                    )
                                    context.startActivity(intent)
                                },
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.92f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                AsyncImage(
                                    model = drink.strDrinkThumb ?: R.drawable.cocktail,
                                    contentDescription = drink.strDrink,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Search Cocktails",
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${drink.strCategory ?: "Unknown"} • ${drink.strGlass ?: "Unknown"}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}