package fr.isen.clara.thegreatestcocktailapp


import android.content.Context

object RatingsManager {
    private const val PREF_NAME = "cocktail_ratings"

    fun getRating(context: Context, drinkId: String): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(drinkId, 0)
    }

    fun setRating(context: Context, drinkId: String, rating: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(drinkId, rating).apply()
    }
}