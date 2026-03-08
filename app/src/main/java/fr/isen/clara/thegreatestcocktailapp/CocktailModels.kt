package fr.isen.clara.thegreatestcocktailapp

data class DrinkResponse(
    val drinks: List<Drink>?
)

data class Drink(
    val idDrink: String,
    val strDrink: String,
    val strCategory: String?,
    val strGlass: String?,
    val strInstructions: String?,
    val strDrinkThumb: String?,

    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?
)

data class CategoryResponse(
    val drinks: List<CategoryItem>
)

data class CategoryItem(
    val strCategory: String
)

data class FilterDrinkResponse(
    val drinks: List<FilterDrinkItem>
)

data class FilterDrinkItem(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String?
)