package fr.isen.clara.thegreatestcocktailapp

import android.content.Context

object FavouritesManager {
    private const val PREF_NAME = "cocktail_favorites"
    private const val KEY_IDS = "favorite_ids"

    fun getFavorites(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_IDS, emptySet()) ?: emptySet()
    }

    fun isFavorite(context: Context, drinkId: String): Boolean {
        return getFavorites(context).contains(drinkId)
    }

    fun toggleFavorite(context: Context, drinkId: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = getFavorites(context).toMutableSet()

        val isNowFavorite = if (current.contains(drinkId)) {
            current.remove(drinkId)
            false
        } else {
            current.add(drinkId)
            true
        }

        prefs.edit().putStringSet(KEY_IDS, current).apply()
        return isNowFavorite
    }
}