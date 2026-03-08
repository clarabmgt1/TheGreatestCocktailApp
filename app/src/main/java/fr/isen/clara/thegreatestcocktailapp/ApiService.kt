package fr.isen.clara.thegreatestcocktailapp

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun getRandomDrink(): DrinkResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getDrinksByCategory(
        @Query("c") category: String
    ): FilterDrinkResponse

    @GET("lookup.php")
    suspend fun getDrinkDetail(
        @Query("i") id: String
    ): DrinkResponse

    @GET("search.php")
    suspend fun searchCocktailByName(
        @Query("s") name: String
    ): DrinkResponse
}