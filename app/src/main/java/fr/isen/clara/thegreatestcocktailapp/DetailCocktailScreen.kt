package fr.isen.clara.thegreatestcocktailapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(
    drink: Drink,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current

    var isFavorite by remember(drink.idDrink) {
        mutableStateOf(
            FavouritesManager.isFavorite(context, drink.idDrink)
        )
    }

    var rating by remember(drink.idDrink) {
        mutableIntStateOf(
            RatingsManager.getRating(context, drink.idDrink)
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = drink.strDrink,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(
                        onClick = {
                            isFavorite = FavouritesManager.toggleFavorite(context, drink.idDrink)

                            Toast.makeText(
                                context,
                                if (isFavorite) "Added to favourites" else "Removed from favourites",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favourite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = drink.strDrinkThumb ?: R.drawable.cocktail,
                contentDescription = drink.strDrink,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = drink.strDrink,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Category: ${drink.strCategory ?: "Unknown"}",
                    color = Color.White
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Glass: ${drink.strGlass ?: "Unknown"}",
                    color = Color.White
                )

                Spacer(Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Your rating",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            for (i in 1..5) {
                                IconButton(
                                    onClick = {
                                        rating = i
                                        RatingsManager.setRating(context, drink.idDrink, i)

                                        Toast.makeText(
                                            context,
                                            "Rating saved: $i/5",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = "Rate $i stars",
                                        tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (rating == 0) "No rating yet" else "Your score: $rating/5"
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        drink.strIngredient1?.let { Text("• $it") }
                        drink.strIngredient2?.let { Text("• $it") }
                        drink.strIngredient3?.let { Text("• $it") }
                        drink.strIngredient4?.let { Text("• $it") }
                        drink.strIngredient5?.let { Text("• $it") }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Recipe", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(drink.strInstructions ?: "No instructions")
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}