package com.kitapacak.wecan.SharedPref

import android.content.Context

internal class CityPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_CITY, Context.MODE_PRIVATE)

    fun setCity(value: CityModel) {
        val editor = preferences.edit()
        editor.putString(CITY, value.city)
        editor.putString(COUNTRY, value.country)
        editor.apply()
    }

    fun getCity() : CityModel {
        val model = CityModel()
        model.city = preferences.getString(CITY, "")
        model.country = preferences.getString(COUNTRY, "")
        return model
    }

    companion object {
        private const val PREFS_CITY = "city_pref"
        private const val CITY = "city"
        private const val COUNTRY = "country"
    }
}