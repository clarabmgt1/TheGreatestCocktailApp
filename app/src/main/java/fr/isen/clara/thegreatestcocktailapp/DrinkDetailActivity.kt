package fr.isen.clara.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*

class DrinkDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drinkId = intent.getStringExtra(EXTRA_DRINK_ID) ?: "No ID"

        setContent {
            DrinkDetailContent(
                drinkId = drinkId,
                onBack = { finish() }
            )
        }
    }

    companion object {
        const val EXTRA_DRINK_ID = "extra_drink_id"
    }
}

@Composable
fun DrinkDetailContent(
    drinkId: String,
    onBack: () -> Unit
) {
    var drink by remember { mutableStateOf<Drink?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(drinkId) {
        try {
            val response = NetworkManager.api.getDrinkDetail(drinkId)
            drink = response.drinks?.firstOrNull()
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error"
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Text("Loading drink detail...")
        }

        errorMessage != null -> {
            Text("Error: $errorMessage")
        }

        drink != null -> {
            DetailCocktailScreen(
                drink = drink!!,
                onBack = onBack
            )
        }

        else -> {
            Text("No drink found")
        }
    }
}